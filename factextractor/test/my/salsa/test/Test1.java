package my.salsa.test;

class ParamTestClass {
    int method1(int a, String b, Integer c) {
        return 0;
    }
    void method2(int param1, String param2, Integer param3, double param4) {
    }
}
class SyncClass {
    synchronized void synchronizedMethod() {
    }

    void methodWithInnerSynchronizedBlock() {
        synchronized(this) {
            int foo = 1;
        }
    }
    
}
class TestStatements {
    public void statementsTest()
    //
    //
    { /////
        int x = 1;
        int y;
        int z;
        while (x == 1) {
            x = 2;

        }
        try {
            x = 3;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

@interface MyDannotation {
    String type();

    String value();
}

class DannotationSalsa {
    public static void dummyCall(int dummy) {

    }
}

/**
 * Generic annotation test class
 */
class Xexe {
    public void voo() {
        @MyDannotation(type = "Scriptlet", value = " String syncToken_nb = (String) request.getAttribute(\"SyncToken\"); ")
                int dummyLocal0;
        DannotationSalsa.dummyCall(1);
    }
}

/**
 * Generic annotation test class
 */
public @ClassAnnotation("my favorite class annotation") class Test1 {

    public @ConstructorAnnotation("my constructor") Test1() {
    }

    public @FieldAnnotation("xexe") int foo;

    @MethodAnnotation (fakeArgument2 = "bar") void boo() {
        @LocalVariableAnnotation int i;
    }

    public void baz(@ParameterAnnotation("my parameter") int someParameter) {
        voo();
        voo();
        class Aaa {
        }
    }

    public void voo() {
        @MyDannotation(type = "Scriptlet", value = " String syncToken_nb = (String) request.getAttribute(\"SyncToken\"); ")
                int $salsa$dummyLocal0;
    }
}

@interface MethodAnnotation {
    String value() default "this is a default method annotation";

    String fakeaArgument1() default "foo";

    String fakeArgument2();
}

@interface ClassAnnotation {
    String value() default "this is a default class annotation";
}

@interface FieldAnnotation {
    String value() default "hello";
}

@interface LocalVariableAnnotation {
    String value() default "this is a default local variable annotation";
}

@interface ParameterAnnotation {
    String value() default "this is a default parameter annotation";
}

@interface ConstructorAnnotation {
    String value() default "this is a default constructor annotation";
}

/**
 * Misc annotation test class (default annotation arguments testing)
 */
class Test2 {
    public @Foo void test() {
    }
}

@interface Foo {
    String value() default "foofoofoo";
}

/**
 * Misc annotation test class (default annotation arguments testing)
 */
class Test3 {
    @Bar(bar1 = "barbarbar1+barbarbar1") int xexe;

    public void testbar(@Bar(bar1 = "barbarbar1+barbarbar1") int x) {
    }

    void boxing() {
        int i = new Integer(1);
        Integer j = 0;
    }
}

@interface Bar {
    String bar1() default "barbarbar1";

    String bar2() default "barbarbar2";
}
