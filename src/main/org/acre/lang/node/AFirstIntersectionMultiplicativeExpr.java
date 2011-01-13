/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstIntersectionMultiplicativeExpr extends PMultiplicativeExpr
{
    private TIdentifier _identifier_;
    private TSetAnd _setAnd_;
    private PTmpInExpr _tmpInExpr_;

    public AFirstIntersectionMultiplicativeExpr()
    {
    }

    public AFirstIntersectionMultiplicativeExpr(
        TIdentifier _identifier_,
        TSetAnd _setAnd_,
        PTmpInExpr _tmpInExpr_)
    {
        setIdentifier(_identifier_);

        setSetAnd(_setAnd_);

        setTmpInExpr(_tmpInExpr_);

    }
    public Object clone()
    {
        return new AFirstIntersectionMultiplicativeExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TSetAnd) cloneNode(_setAnd_),
            (PTmpInExpr) cloneNode(_tmpInExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstIntersectionMultiplicativeExpr(this);
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

    public TSetAnd getSetAnd()
    {
        return _setAnd_;
    }

    public void setSetAnd(TSetAnd node)
    {
        if(_setAnd_ != null)
        {
            _setAnd_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _setAnd_ = node;
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
            + toString(_setAnd_)
            + toString(_tmpInExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_setAnd_ == child)
        {
            _setAnd_ = null;
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

        if(_setAnd_ == oldChild)
        {
            setSetAnd((TSetAnd) newChild);
            return;
        }

        if(_tmpInExpr_ == oldChild)
        {
            setTmpInExpr((PTmpInExpr) newChild);
            return;
        }

    }
}