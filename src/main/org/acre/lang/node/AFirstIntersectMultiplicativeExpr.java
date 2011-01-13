/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstIntersectMultiplicativeExpr extends PMultiplicativeExpr
{
    private TIdentifier _identifier_;
    private TIntersect _intersect_;
    private PTmpInExpr _tmpInExpr_;

    public AFirstIntersectMultiplicativeExpr()
    {
    }

    public AFirstIntersectMultiplicativeExpr(
        TIdentifier _identifier_,
        TIntersect _intersect_,
        PTmpInExpr _tmpInExpr_)
    {
        setIdentifier(_identifier_);

        setIntersect(_intersect_);

        setTmpInExpr(_tmpInExpr_);

    }
    public Object clone()
    {
        return new AFirstIntersectMultiplicativeExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TIntersect) cloneNode(_intersect_),
            (PTmpInExpr) cloneNode(_tmpInExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstIntersectMultiplicativeExpr(this);
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
            + toString(_identifier_)
            + toString(_intersect_)
            + toString(_tmpInExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
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
        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
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