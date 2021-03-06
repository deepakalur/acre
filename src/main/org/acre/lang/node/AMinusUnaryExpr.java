/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AMinusUnaryExpr extends PUnaryExpr
{
    private TMinus _minus_;
    private PUnaryExpr _unaryExpr_;

    public AMinusUnaryExpr()
    {
    }

    public AMinusUnaryExpr(
        TMinus _minus_,
        PUnaryExpr _unaryExpr_)
    {
        setMinus(_minus_);

        setUnaryExpr(_unaryExpr_);

    }
    public Object clone()
    {
        return new AMinusUnaryExpr(
            (TMinus) cloneNode(_minus_),
            (PUnaryExpr) cloneNode(_unaryExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMinusUnaryExpr(this);
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

    public PUnaryExpr getUnaryExpr()
    {
        return _unaryExpr_;
    }

    public void setUnaryExpr(PUnaryExpr node)
    {
        if(_unaryExpr_ != null)
        {
            _unaryExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _unaryExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_minus_)
            + toString(_unaryExpr_);
    }

    void removeChild(Node child)
    {
        if(_minus_ == child)
        {
            _minus_ = null;
            return;
        }

        if(_unaryExpr_ == child)
        {
            _unaryExpr_ = null;
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

        if(_unaryExpr_ == oldChild)
        {
            setUnaryExpr((PUnaryExpr) newChild);
            return;
        }

    }
}
