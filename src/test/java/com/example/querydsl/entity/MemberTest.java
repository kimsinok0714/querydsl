package com.example.querydsl.entity;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
// @Commit
public class MemberTest {
    @Autowired
    EntityManager em;

    @Test
    public void testEntity() {

        Team a = new Team("a");
        Team b = new Team("b");

        em.persist(a);
        em.persist(b);

        System.out.println("a : " + a);

        Member m1 = new Member("kim", 10, a);
        Member m2 = new Member("hong", 20, a);
        Member m3 = new Member("lee", 30, b);
        Member m4 = new Member("park", 40, b);

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.persist(m4);

        // 초기화
        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("member : " + member);
            System.out.println("member.team : " + member.getTeam());
        }

    }

}
