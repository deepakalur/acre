/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AUnaryAbsExpr extends PExpr
{
    private TAbs _abs_;
    private PExpr _expr_;

    public AUnaryAbsExpr()
    {
    }

    public AUnaryAbsExpr(
        TAbs _abs_,
        PExpr _expr_)
    {
        setAbs(_abs_);

        setExpr(_expr_);

    }
    public Object clone()
    {
        return new AUnaryAbsExpr(
            (TAbs) cloneNode(_abs_),
            (PExpr) cloneNode(_expr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAUnaryAbsExpr(this);
    }

    public TAbs getAbs()
    {
        return _abs_;
    }

    public void setAbs(TAbs node)
    {
        if(_abs_ != null)
        {
            _abs_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _abs_ = node;
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
            + toString(_abs_)
            + toString(_expr_);
    }

    void removeChild(Node child)
    {
        if(_abs_ == child)
        {
            _abs_ = null;
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
        if(_abs_ == oldChild)
        {
            setAbs((TAbs) newChild);
            return;
        }

        if(_expr_ == oldChild)
        {
            setExpr((PExpr) newChild);
            return;
        }

    }
}