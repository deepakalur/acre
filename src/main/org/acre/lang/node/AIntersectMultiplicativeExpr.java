/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIntersectMultiplicativeExpr extends PMultiplicativeExpr
{
    private PMultiplicativeExpr _multiplicativeExpr_;
    private TIntersect _intersect_;
    private PTmpInExpr _tmpInExpr_;

    public AIntersectMultiplicativeExpr()
    {
    }

    public AIntersectMultiplicativeExpr(
        PMultiplicativeExpr _multiplicativeExpr_,
        TIntersect _intersect_,
        PTmpInExpr _tmpInExpr_)
    {
        setMultiplicativeExpr(_multiplicativeExpr_);

        setIntersect(_intersect_);

        setTmpInExpr(_tmpInExpr_);

    }
    public Object clone()
    {
        return new AIntersectMultiplicativeExpr(
            (PMultiplicativeExpr) cloneNode(_multiplicativeExpr_),
            (TIntersect) cloneNode(_intersect_),
            (PTmpInExpr) cloneNode(_tmpInExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIntersectMultiplicativeExpr(this);
    }

    public PMultiplicativeExpr getMultiplicativeExpr()
    {
        return _multiplicativeExpr_;
    }

    public void setMultiplicativeExpr(PMultiplicativeExpr node)
    {
        if(_multiplicativeExpr_ != null)
        {
            _multiplicativeExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _multiplicativeExpr_ = node;
    }

    public TIntersect getIntersect()
    {
        return _intersect_;
    }

    public void setIntersect(TIntersect node)
    {
        if(_intersect_ != null)
        {
            _intersect_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _intersect_ = node;
    }

    public PTmpInExpr getTmpInExpr()
    {
        return _tmpInExpr_;
    }

    public void setTmpInExpr(PTmpInExpr node)
    {
        if(_tmpInExpr_ != null)
        {
            _tmpInExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpInExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_multiplicativeExpr_)
            + toString(_intersect_)
            + toString(_tmpInExpr_);
    }

    void removeChild(Node child)
    {
        if(_multiplicativeExpr_ == child)
        {
            _multiplicativeExpr_ = null;
            return;
        }

        if(_intersect_ == child)
        {
            _intersect_ = null;
            return;
        }

        if(_tmpInExpr_ == child)
        {
            _tmpInExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_multiplicativeExpr_ == oldChild)
        {
            setMultiplicativeExpr((PMultiplicativeExpr) newChild);
            return;
        }

        if(_intersect_ == oldChild)
        {
            setIntersect((TIntersect) newChild);
            return;
        }

        if(_tmpInExpr_ == oldChild)
        {
            setTmpInExpr((PTmpInExpr) newChild);
            return;
        }

    }
}