/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AUnaryMinusExpr extends PExpr
{
    private TMinus _minus_;
    private PExpr _expr_;

    public AUnaryMinusExpr()
    {
    }

    public AUnaryMinusExpr(
        TMinus _minus_,
        PExpr _expr_)
    {
        setMinus(_minus_);

        setExpr(_expr_);

    }
    public Object clone()
    {
        return new AUnaryMinusExpr(
            (TMinus) cloneNode(_minus_),
            (PExpr) cloneNode(_expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAUnaryMinusExpr(this);
    }

    public TMinus getMinus()
    {
        return _minus_;
    }

    public void setMinus(TMinus node)
    {
        if(_minus_ != null)
        {
            _minus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _minus_ = node;
    }

    public PExpr getExpr()
    {
        return _expr_;
    }

    public void setExpr(PExpr node)
    {
        if(_expr_ != null)
        {
            _expr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _expr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_minus_)
            + toString(_expr_);
    }

    void removeChild(Node child)
    {
        if(_minus_ == child)
        {
            _minus_ = null;
            return;
        }

        if(_expr_ == child)
        {
            _expr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_minus_ == oldChild)
        {
            setMinus((TMinus) newChild);
            return;
        }

        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

    }
}
