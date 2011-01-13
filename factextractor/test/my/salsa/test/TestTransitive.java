package my.salsa.test;

/*
 * Copyright (c) 2004, 2005 Sun Microsystems. All Rights Reserved.
 */


public class TestTransitive {
    void a() {
        b();
    }

    void b() {
        c();
    }

    void c() {
    }

    void x() {
        y();
    }
    void y() {
        z();
    }
    void z() {
        x();
    }
    void q() {
        q();
    }

}
