package com.wolfhouse.journeydaily;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class JourneyDailyApplicationTests {
/*    @Test
    void beanUtilCopyProperties() {
        UserTest u1 = new UserTest();
        u1.setName("linexsong");
        u1.setEmail("3o8Qm@example.com");
        u1.setPassword("");
        UserTest u2 = BeanUtil.copyProperties(u1, UserTest.class, true);
        System.out.println(u2)
    }*/

/*    @Test
    void testJwt() {
        String sec = "123";
        Map<String, Object> m = new HashMap<>();
        m.put("name", "linexsong");
        String jwt = JwtUtil.createJWT(sec, 1000, m);
        System.out.println(jwt);
        Claims claims = JwtUtil.parseToken(jwt, sec);
        System.out.println(claims.get("name"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Claims claims1 = JwtUtil.parseToken(jwt, sec);
        System.out.println(claims1)
    }*/

    //@Test
    //void beanUtilsTests() {
    //    Boolean b = null;
    //    Boolean c = true;
    //    Boolean d = false;
    //    System.out.println(b);
    //    System.out.println(Arrays.toString(BeanUtil.checkBlank(b, c, d)));
    //    System.out.println(BeanUtil.isAnyNotBlank(b, c, d));
    //}

    //@Test
    //void beanUtilsTests() {
    //    List<User> users = new ArrayList<>();
    //    users.add(User.builder().userName("123").build());
    //    users.add(User.builder().userName("12").build());
    //    users.add(User.builder().userName("1").build());
    //    System.out.println(BeanUtil.copyList(users, UserVo.class));
    //}

    //@Autowired
    //JourneyService service;
    //
    //@Test
    //void draftTest() {
    //    JourneyQueryDto dto = new JourneyQueryDto();
    //    dto.setPageSize(50);
    //    System.out.println(service.getJourneys(dto).getTotal());
    //}

}
