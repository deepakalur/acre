/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import org.acre.lang.analysis.*;

public final class TFlatten extends Token
{
    public TFlatten(String text)
    {
        setText(text);
    }

    public TFlatten(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    public Object clone()
    {
      return new TFlatten(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTFlatten(this);
    }
}
