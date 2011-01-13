/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstPlusAdditiveExpr extends PAdditiveExpr
{
    private TIdentifier _identifier_;
    private TPlus _plus_;
    private PMultiplicativeExpr _multiplicativeExpr_;

    public AFirstPlusAdditiveExpr()
    {
    }

    public AFirstPlusAdditiveExpr(
        TIdentifier _identifier_,
        TPlus _plus_,
        PMultiplicativeExpr _multiplicativeExpr_)
    {
        setIdentifier(_identifier_);

        setPlus(_plus_);

        setMultiplicativeExpr(_multiplicativeExpr_);

    }
    public Object clone()
    {
        return new AFirstPlusAdditiveExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TPlus) cloneNode(_plus_),
            (PMultiplicativeExpr) cloneNode(_multiplicativeExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstPlusAdditiveExpr(this);
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

    public TPlus getPlus()
    {
        return _plus_;
    }

    public void setPlus(TPlus node)
    {
        if(_plus_ != null)
        {
            _plus_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _plus_ = node;
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

    public String toString()
    {
        return ""
            + toString(_identifier_)
            + toString(_plus_)
            + toString(_multiplicativeExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_plus_ == child)
        {
            _plus_ = null;
            return;
        }

        if(_multiplicativeExpr_ == child)
        {
            _multiplicativeExpr_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_plus_ == oldChild)
        {
            setPlus((TPlus) newChild);
            return;
        }

        if(_multiplicativeExpr_ == oldChild)
        {
            setMultiplicativeExpr((PMultiplicativeExpr) newChild);
            return;
        }

    }
}
