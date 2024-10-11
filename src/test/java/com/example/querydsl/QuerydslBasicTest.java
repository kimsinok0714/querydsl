package com.example.querydsl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.querydsl.entity.Member;
import com.example.querydsl.entity.QMember;
import com.example.querydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    public void before() {
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
    }

    // @Test
    // public void startJPQL() {
    // Member findMember = em.createQuery("select m from Member m where m.userName =
    // :userName", Member.class)
    // .setParameter("userName", "kim")
    // .getSingleResult();

    // assertThat(findMember.getUserName()).isEqualTo("kim");

    // }

    // @Test
    // public void startQuerydsl() {

    // JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    // // QMember m = new QMember("m"); // 별칭
    // QMember m = QMember.member;

    // // 컴파일 시 오류를 해결할 수 있다.
    // Member findMember = queryFactory
    // .select(m)
    // .from(m)
    // .where(m.userName.eq("kim"))
    // .fetchOne();

    // assertThat(findMember.getUserName()).isEqualTo("kim");
    // }

    // @Test
    // public void search() {
    // JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    // QMember m = QMember.member;

    // Member findMember = queryFactory.selectFrom(m)
    // .where(m.userName.eq("kim").and(m.age.between(10, 30)))
    // .fetchOne();

    // assertThat(findMember.getUserName()).isEqualTo("kim");
    // }

    // @Test
    // public void resultFetch() {

    // JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    // QMember m = QMember.member;

    // // List<Member> members = queryFactory
    // // .selectFrom(m)
    // // .fetch();

    // // Member member = queryFactory
    // // .selectFrom(m)
    // // .fetchFirst();

    // // QueryResults<Member> results = queryFactory
    // // .selectFrom(m)
    // // .fetchResults();

    // // results.getTotal();
    // // List<Member> content = results.getResults();

    // long total = queryFactory
    // .selectFrom(m)
    // .fetchCount();

    // }

    // @Test
    // public void sort() {

    // JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    // QMember m = QMember.member;

    // em.persist(new Member(null, 100));
    // em.persist(new Member("ann", 100));
    // em.persist(new Member("jang", 100));

    // List<Member> result = queryFactory
    // .selectFrom(m)
    // .where(m.age.eq(100))
    // .orderBy(m.age.desc(), m.userName.asc().nullsLast())
    // .fetch();

    // Member m1 = result.get(0);
    // Member m2 = result.get(1);
    // Member m3 = result.get(2);

    // assertThat(m1.getUserName()).isEqualTo("ann");
    // assertThat(m2.getUserName()).isEqualTo("jang");
    // assertThat(m3.getUserName()).isNull();

    // }

    @Test
    public void paging1() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember member = QMember.member;

        // The method fetchCount() from the type AbstractJPAQuery<Member,
        // JPAQuery<Member>> is deprecated 문제 발생
        // QueryResults<Member> result = queryFactory.selectFrom(member)
        // .orderBy(member.userName.asc())
        // .offset(1) // offset은 0부터 시작
        // .limit(2)
        // .fetchResults();

        int page = 3;
        int size = 10;

        List<Member> members = queryFactory
                .selectFrom(member)
                .offset(page * size)
                .limit(size)
                .fetch();

        long total = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();
    }

    @Test
    public void aggregation() {

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QMember member = QMember.member;

        // com.querydsl.core.Tuple Tuple 자료형 보다 DTO 자료형을 선호한다.
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);

        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);

    }

}
