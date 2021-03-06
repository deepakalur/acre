/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastRlikeTmpEqualityExpr extends PTmpEqualityExpr
{
    private PTmpEqualityExpr _tmpEqualityExpr_;
    private TRlike _rlike_;
    private TIdentifier _identifier_;

    public ALastRlikeTmpEqualityExpr()
    {
    }

    public ALastRlikeTmpEqualityExpr(
        PTmpEqualityExpr _tmpEqualityExpr_,
        TRlike _rlike_,
        TIdentifier _identifier_)
    {
        setTmpEqualityExpr(_tmpEqualityExpr_);

        setRlike(_rlike_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastRlikeTmpEqualityExpr(
            (PTmpEqualityExpr) cloneNode(_tmpEqualityExpr_),
            (TRlike) cloneNode(_rlike_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastRlikeTmpEqualityExpr(this);
    }

    public PTmpEqualityExpr getTmpEqualityExpr()
    {
        return _tmpEqualityExpr_;
    }

    public void setTmpEqualityExpr(PTmpEqualityExpr node)
    {
        if(_tmpEqualityExpr_ != null)
        {
            _tmpEqualityExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpEqualityExpr_ = node;
    }

    public TRlike getRlike()
    {
        return _rlike_;
    }

    public void setRlike(TRlike node)
    {
        if(_rlike_ != null)
        {
            _rlike_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _rlike_ = node;
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
            + toString(_tmpEqualityExpr_)
            + toString(_rlike_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpEqualityExpr_ == child)
        {
            _tmpEqualityExpr_ = null;
            return;
        }

        if(_rlike_ == child)
        {
            _rlike_ = null;
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
        if(_tmpEqualityExpr_ == oldChild)
        {
            setTmpEqualityExpr((PTmpEqualityExpr) newChild);
            return;
        }

        if(_rlike_ == oldChild)
        {
            setRlike((TRlike) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}
