/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastIntersectMultiplicativeExpr extends PMultiplicativeExpr
{
    private PMultiplicativeExpr _multiplicativeExpr_;
    private TIntersect _intersect_;
    private TIdentifier _identifier_;

    public ALastIntersectMultiplicativeExpr()
    {
    }

    public ALastIntersectMultiplicativeExpr(
        PMultiplicativeExpr _multiplicativeExpr_,
        TIntersect _intersect_,
        TIdentifier _identifier_)
    {
        setMultiplicativeExpr(_multiplicativeExpr_);

        setIntersect(_intersect_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastIntersectMultiplicativeExpr(
            (PMultiplicativeExpr) cloneNode(_multiplicativeExpr_),
            (TIntersect) cloneNode(_intersect_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastIntersectMultiplicativeExpr(this);
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

    public TIdentifier getIdentifier()
    {
        return _identifier_;
    }

    public void setIdentifier(TIdentifier node)
    {
        if(_identifier_ != null)
        {
            _identifier_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifier_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_multiplicativeExpr_)
            + toString(_intersect_)
            + toString(_identifier_);
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

        if(_identifier_ == child)
        {
            _identifier_ = null;
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

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}