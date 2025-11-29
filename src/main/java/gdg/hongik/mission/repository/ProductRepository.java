package gdg.hongik.mission.repository;


import gdg.hongik.mission.dto.Product;
import gdg.hongik.mission.service.ProductService;
import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {


    @PersistenceContext
    EntityManager em;

    public Product findById(Long id){
        return em.find(Product.class, id);
    }

    public Product findByName(String name){
        List<Product> result = em.createQuery(
                "SELECT p FROM Product p WHERE p.name = :name ", Product.class
        ).setParameter("name",name).getResultList();

        return result.isEmpty()? null : result.get(0);
    }

    public void save(Product product){

        em.persist(product);
    }

    public void deleteByName(String name){
        List<Product> product = em.createQuery(
                "SELECT p FROM Product p WHERE p.name = :name", Product.class
        ).setParameter("name",name).getResultList();

        em.remove(product.get(0));
    }

    public List<Product> findAll(){

        return em.createQuery(
                "SELECT p FROM Product p ",Product.class
        ).getResultList();

    }


/*
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }


    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public Member findByLoginId(String loginId) {
        List<Member> result = em.createQuery(
                "SELECT m FROM Member m WHERE m.loginId = :loginId", Member.class
        ).setParameter("loginId", loginId).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }
    public Member findByLoginId(String loginId) {
        List<Member> result = em.createQuery(
                "SELECT m FROM Member m WHERE m.loginId = :loginId", Member.class
        ).setParameter("loginId", loginId).getResultList();

        return result.isEmpty() ? null : result.get(0);
    }


    public void save(Member member){
        em.persist(member);
    }

    public void deleteById(Long id){
        Member member = em.find(Member.class, id);
        em.remove(member);
    }


*/


}
