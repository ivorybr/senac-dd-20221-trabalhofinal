package petshop.model.service;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jetbrains.annotations.NotNull;
import petshop.connection.JpaConnectionFactory;
import petshop.exceptions.AtributosInvalidosException;
import petshop.exceptions.RegistroNaoEncontradoException;
import petshop.model.dao.PetDAO;
import petshop.model.dtos.PetDTO;
import petshop.model.entity.Pet;

@ApplicationScoped
public class PetService {
	
	private static Logger LOG = LogManager.getLogger(PetService.class);

	private EntityManager entityManager = new JpaConnectionFactory().getEntityManager();
	
	private PetDAO petDAO;
	
	public PetService() {
		this.petDAO = new PetDAO(this.entityManager);
	}

	public void save(PetDTO petDTO) throws SQLException, AtributosInvalidosException {
		LOG.info("Preparando para salvar o Pet de nome: "+petDTO.getNome());
		validarAtributos(petDTO);

		Pet pet = new Pet();

		setAtributosPet(petDTO, pet);

		abrirConexaoBanco();
		this.petDAO.save(pet);
		commitarTransacaoBanco();
		LOG.info("Pet adicionado com sucesso!");
	}

	public void update(Long idPet, PetDTO petDTO) throws RegistroNaoEncontradoException {
		LOG.info("Preparando para atualizar o Pet de id:"+idPet);

		if(!petDAO.petExists(idPet)){
			throw new RegistroNaoEncontradoException("Pet de ID: "+ idPet +" não encontrado na base de dados!");
		}

		Pet pet = petDAO.find(Pet.class, idPet);

		setAtributosPet(petDTO, pet);

		abrirConexaoBanco();
		this.petDAO.merge(pet);
		commitarTransacaoBanco();

		LOG.info("Pet atualizado com sucesso!");
	}

	public List<PetDTO> listAll(){
		LOG.info("Procurando todos os pets cadastrados");
		List<Pet> petsEntity = this.petDAO.findAll();
		List<PetDTO> pets = new ArrayList<>();

		for(Pet pet : petsEntity){
			pets.add(new PetDTO(pet));
		}

		LOG.info("Foram encontrados "+pets.size()+" pets.");

		return pets;
	}

	//TODO fazer delete lógico do pet

	private void setAtributosPet(PetDTO petDTO, Pet pet) {
		if(petDTO.getNome() != null)
			pet.setNome(petDTO.getNome());

		if(petDTO.getDataNascimento() != null)
			pet.setDataNascimento(petDTO.getDataNascimento());

		if(petDTO.getNomeDono() != null)
			pet.setNomeDono(petDTO.getNomeDono());

		if(petDTO.getRaca() != null)
			pet.setRaca(petDTO.getRaca());

		if(petDTO.getTipoAnimal() != null)
			pet.setTipoAnimal(petDTO.getTipoAnimal());

		if(petDTO.getPontosFidelidade() > 0)
			pet.setPontosFidelidade(petDTO.getPontosFidelidade());
	}

	private void validarAtributos(PetDTO pet) throws AtributosInvalidosException {
		LOG.info("Validando os atributos do Pet");
		List<String> messages = new ArrayList<>();

		if(pet.getNome().isBlank() || pet.getNome() == null){
			messages.add("Nome não pode estar em branco ou nulo!");
		}

		if(pet.getDataNascimento() == null){
			messages.add("Informe a data de nascimento!");
		}

		if(pet.getRaca().isBlank() || pet.getRaca() == null){
			messages.add("Raça não pode estar em branco ou nulo!");
		}

		if(pet.getTipoAnimal() == null){
			messages.add("O tipo de animal deve ser selecionado!");
		}

		if(!messages.isEmpty()){
			LOG.error("Erro ao validar os atributos. Algum atributo inválido!");
			throw new AtributosInvalidosException(messages.toString());
		}
	}

	private void commitarTransacaoBanco() {
		this.petDAO.getEntityManager().getTransaction().commit();
	}

	private void abrirConexaoBanco() {
		this.petDAO.getEntityManager().getTransaction().begin();
	}

}
