/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ACharLiteral extends PLiteral
{
    private TCharLiteral _charLiteral_;

    public ACharLiteral()
    {
    }

    public ACharLiteral(
        TCharLiteral _charLiteral_)
    {
        setCharLiteral(_charLiteral_);

    }
    public Object clone()
    {
        return new ACharLiteral(
            (TCharLiteral) cloneNode(_charLiteral_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseACharLiteral(this);
    }

    public TCharLiteral getCharLiteral()
    {
        return _charLiteral_;
    }

    public void setCharLiteral(TCharLiteral node)
    {
        if(_charLiteral_ != null)
        {
            _charLiteral_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _charLiteral_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_charLiteral_);
    }

    void removeChild(Node child)
    {
        if(_charLiteral_ == child)
        {
            _charLiteral_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_charLiteral_ == oldChild)
        {
            setCharLiteral((TCharLiteral) newChild);
            return;
        }

    }
}
