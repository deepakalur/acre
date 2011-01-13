/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastExceptAdditiveExpr extends PAdditiveExpr
{
    private PAdditiveExpr _additiveExpr_;
    private TExcept _except_;
    private TIdentifier _identifier_;

    public ALastExceptAdditiveExpr()
    {
    }

    public ALastExceptAdditiveExpr(
        PAdditiveExpr _additiveExpr_,
        TExcept _except_,
        TIdentifier _identifier_)
    {
        setAdditiveExpr(_additiveExpr_);

        setExcept(_except_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastExceptAdditiveExpr(
            (PAdditiveExpr) cloneNode(_additiveExpr_),
            (TExcept) cloneNode(_except_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastExceptAdditiveExpr(this);
    }

    public PAdditiveExpr getAdditiveExpr()
    {
        return _additiveExpr_;
    }

    public void setAdditiveExpr(PAdditiveExpr node)
    {
        if(_additiveExpr_ != null)
        {
            _additiveExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _additiveExpr_ = node;
    }

    public TExcept getExcept()
    {
        return _except_;
    }

    public void setExcept(TExcept node)
    {
        if(_except_ != null)
        {
            _except_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _except_ = node;
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
            + toString(_additiveExpr_)
            + toString(_except_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_additiveExpr_ == child)
        {
            _additiveExpr_ = null;
            return;
        }

        if(_except_ == child)
        {
            _except_ = null;
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
        if(_additiveExpr_ == oldChild)
        {
            setAdditiveExpr((PAdditiveExpr) newChild);
            return;
        }

        if(_except_ == oldChild)
        {
            setExcept((TExcept) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}