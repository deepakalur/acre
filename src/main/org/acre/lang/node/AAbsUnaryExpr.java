/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AAbsUnaryExpr extends PUnaryExpr
{
    private TAbs _abs_;
    private PUnaryExpr _unaryExpr_;

    public AAbsUnaryExpr()
    {
    }

    public AAbsUnaryExpr(
        TAbs _abs_,
        PUnaryExpr _unaryExpr_)
    {
        setAbs(_abs_);

        setUnaryExpr(_unaryExpr_);

    }
    public Object clone()
    {
        return new AAbsUnaryExpr(
            (TAbs) cloneNode(_abs_),
            (PUnaryExpr) cloneNode(_unaryExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAAbsUnaryExpr(this);
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
            + toString(_abs_)
            + toString(_unaryExpr_);
    }

    void removeChild(Node child)
    {
        if(_abs_ == child)
        {
            _abs_ = null;
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
        if(_abs_ == oldChild)
        {
            setAbs((TAbs) newChild);
            return;
        }

        if(_unaryExpr_ == oldChild)
        {
            setUnaryExpr((PUnaryExpr) newChild);
            return;
        }

    }
}
