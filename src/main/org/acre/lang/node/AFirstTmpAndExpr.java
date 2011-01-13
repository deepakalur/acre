/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstTmpAndExpr extends PTmpAndExpr
{
    private TIdentifier _identifier_;
    private TAnd _and_;
    private PQuantifierExpr _quantifierExpr_;

    public AFirstTmpAndExpr()
    {
    }

    public AFirstTmpAndExpr(
        TIdentifier _identifier_,
        TAnd _and_,
        PQuantifierExpr _quantifierExpr_)
    {
        setIdentifier(_identifier_);

        setAnd(_and_);

        setQuantifierExpr(_quantifierExpr_);

    }
    public Object clone()
    {
        return new AFirstTmpAndExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TAnd) cloneNode(_and_),
            (PQuantifierExpr) cloneNode(_quantifierExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstTmpAndExpr(this);
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

    public TAnd getAnd()
    {
        return _and_;
    }

    public void setAnd(TAnd node)
    {
        if(_and_ != null)
        {
            _and_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _and_ = node;
    }

    public PQuantifierExpr getQuantifierExpr()
    {
        return _quantifierExpr_;
    }

    public void setQuantifierExpr(PQuantifierExpr node)
    {
        if(_quantifierExpr_ != null)
        {
            _quantifierExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _quantifierExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_identifier_)
            + toString(_and_)
            + toString(_quantifierExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_and_ == child)
        {
            _and_ = null;
            return;
        }

        if(_quantifierExpr_ == child)
        {
            _quantifierExpr_ = null;
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

        if(_and_ == oldChild)
        {
            setAnd((TAnd) newChild);
            return;
        }

        if(_quantifierExpr_ == oldChild)
        {
            setQuantifierExpr((PQuantifierExpr) newChild);
            return;
        }

    }
}
