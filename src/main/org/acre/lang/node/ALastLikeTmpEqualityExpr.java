/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastLikeTmpEqualityExpr extends PTmpEqualityExpr
{
    private PTmpEqualityExpr _tmpEqualityExpr_;
    private TLike _like_;
    private TIdentifier _identifier_;

    public ALastLikeTmpEqualityExpr()
    {
    }

    public ALastLikeTmpEqualityExpr(
        PTmpEqualityExpr _tmpEqualityExpr_,
        TLike _like_,
        TIdentifier _identifier_)
    {
        setTmpEqualityExpr(_tmpEqualityExpr_);

        setLike(_like_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastLikeTmpEqualityExpr(
            (PTmpEqualityExpr) cloneNode(_tmpEqualityExpr_),
            (TLike) cloneNode(_like_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastLikeTmpEqualityExpr(this);
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

    public TLike getLike()
    {
        return _like_;
    }

    public void setLike(TLike node)
    {
        if(_like_ != null)
        {
            _like_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _like_ = node;
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
            + toString(_like_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpEqualityExpr_ == child)
        {
            _tmpEqualityExpr_ = null;
            return;
        }

        if(_like_ == child)
        {
            _like_ = null;
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

        if(_like_ == oldChild)
        {
            setLike((TLike) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}
