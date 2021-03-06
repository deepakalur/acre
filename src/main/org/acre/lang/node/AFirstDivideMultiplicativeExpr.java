/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstDivideMultiplicativeExpr extends PMultiplicativeExpr
{
    private TIdentifier _identifier_;
    private TDivide _divide_;
    private PTmpInExpr _tmpInExpr_;

    public AFirstDivideMultiplicativeExpr()
    {
    }

    public AFirstDivideMultiplicativeExpr(
        TIdentifier _identifier_,
        TDivide _divide_,
        PTmpInExpr _tmpInExpr_)
    {
        setIdentifier(_identifier_);

        setDivide(_divide_);

        setTmpInExpr(_tmpInExpr_);

    }
    public Object clone()
    {
        return new AFirstDivideMultiplicativeExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TDivide) cloneNode(_divide_),
            (PTmpInExpr) cloneNode(_tmpInExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstDivideMultiplicativeExpr(this);
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

    public TDivide getDivide()
    {
        return _divide_;
    }

    public void setDivide(TDivide node)
    {
        if(_divide_ != null)
        {
            _divide_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _divide_ = node;
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
            + toString(_identifier_)
            + toString(_divide_)
            + toString(_tmpInExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_divide_ == child)
        {
            _divide_ = null;
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
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_divide_ == oldChild)
        {
            setDivide((TDivide) newChild);
            return;
        }

        if(_tmpInExpr_ == oldChild)
        {
            setTmpInExpr((PTmpInExpr) newChild);
            return;
        }

    }
}
