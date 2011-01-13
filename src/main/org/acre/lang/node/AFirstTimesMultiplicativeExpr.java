/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstTimesMultiplicativeExpr extends PMultiplicativeExpr
{
    private TIdentifier _identifier_;
    private TStar _star_;
    private PTmpInExpr _tmpInExpr_;

    public AFirstTimesMultiplicativeExpr()
    {
    }

    public AFirstTimesMultiplicativeExpr(
        TIdentifier _identifier_,
        TStar _star_,
        PTmpInExpr _tmpInExpr_)
    {
        setIdentifier(_identifier_);

        setStar(_star_);

        setTmpInExpr(_tmpInExpr_);

    }
    public Object clone()
    {
        return new AFirstTimesMultiplicativeExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TStar) cloneNode(_star_),
            (PTmpInExpr) cloneNode(_tmpInExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstTimesMultiplicativeExpr(this);
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

    public TStar getStar()
    {
        return _star_;
    }

    public void setStar(TStar node)
    {
        if(_star_ != null)
        {
            _star_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _star_ = node;
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
            + toString(_star_)
            + toString(_tmpInExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_star_ == child)
        {
            _star_ = null;
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

        if(_star_ == oldChild)
        {
            setStar((TStar) newChild);
            return;
        }

        if(_tmpInExpr_ == oldChild)
        {
            setTmpInExpr((PTmpInExpr) newChild);
            return;
        }

    }
}
