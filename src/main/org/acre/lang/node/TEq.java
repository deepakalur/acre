/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TEq extends Token
{
    public TEq()
    {
        super.setText("=");
    }

    public TEq(int line, int pos)
    {
        super.setText("=");
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TEq(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTEq(this);
    }

    public void setText(String text)
    {
        throw new RuntimeException("Cannot change TEq text.");
    }
}
