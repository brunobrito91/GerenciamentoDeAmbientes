package com.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.model.Ambiente;
import com.model.Ambiente.MedicaoTemperatura;

@Stateless
public class AmbienteDAO extends GenericDAOImpl<Ambiente> {
	public AmbienteDAO() {
		super(Ambiente.class);
		System.out.println("AmbienteDAO");
	}

	public void delete(Ambiente ambiente) {
		super.delete(ambiente.getId(), Ambiente.class);
		System.out.println("AmbienteDAO - delete");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Ambiente> findAll() {
		System.out.println("GenericDAOImpl - findAll ");
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery cq = criteriaBuilder.createQuery();
		Root<Ambiente> select = cq.from(Ambiente.class);
		cq.select(select);
		cq.orderBy(criteriaBuilder.asc(select.get("ordem")));
		return em.createQuery(cq).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<MedicaoTemperatura> findMedicoesByDate(int id, Date data) {
		System.out.println("AmbienteDAO - findMedicoesByDate");
		List<MedicaoTemperatura> result = null;

		try {
			Query query = em.createNativeQuery(
					"SELECT * FROM gerenciamentodeambientes.ambiente_medicoestemperatura where Ambiente_id = :id  and dataMedicao between :dataInicio and :dataFim");

			query.setParameter("id", id);
			query.setParameter("dataInicio", data);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			query.setParameter("dataFim", calendar.getTime());

			List<MedicaoTemperatura> medicoesTemperatura = new ArrayList<MedicaoTemperatura>();

			for (Object[] row : (List<Object[]>) query.getResultList()) {
				MedicaoTemperatura medicaoTemperatura = new MedicaoTemperatura((String) row[3], (Date) row[2]);
				medicoesTemperatura.add(medicaoTemperatura);
			}
			return medicoesTemperatura;

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	public void saveMedicao(int idAmbiente, MedicaoTemperatura medicao) {
		Query query = em.createNativeQuery(
				"INSERT INTO gerenciamentodeambientes.ambiente_medicoestemperatura(Ambiente_id, dataLong, dataMedicao, temperatura ) VALUES(:id, :dataLong, :dataMedicao, :temperatura)");
		query.setParameter("id", idAmbiente);
		query.setParameter("dataLong", medicao.getDataLong());
		query.setParameter("dataMedicao", medicao.getDataMedicao());
		query.setParameter("temperatura", medicao.getTemperatura());

		query.executeUpdate();
	}

	public void saveEmail(int idAmbiente, String email) {
		Query query = em.createNativeQuery(
				"INSERT INTO gerenciamentodeambientes.ambiente_email(Ambiente_id, emails_email) VALUES(:id, :email)");
		query.setParameter("id", idAmbiente);
		query.setParameter("email", email);

		query.executeUpdate();
	}

	public void deleteEmail(int idAmbiente, String email) {
		Query query = em.createNativeQuery(
				"DELETE FROM gerenciamentodeambientes.ambiente_email WHERE Ambiente_id=:id AND emails_email=:email");
		query.setParameter("id", idAmbiente);
		query.setParameter("email", email);

		query.executeUpdate();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Ambiente> findAllAtivos() {
		System.out.println("GenericDAOImpl - findAll ");
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery cq = criteriaBuilder.createQuery();
		Root<Ambiente> select = cq.from(Ambiente.class);
		cq.select(select);
		cq.where(criteriaBuilder.equal(select.get("ativo"), 1));
		cq.orderBy(criteriaBuilder.asc(select.get("ordem")));
		return em.createQuery(cq).getResultList();
	}

}
