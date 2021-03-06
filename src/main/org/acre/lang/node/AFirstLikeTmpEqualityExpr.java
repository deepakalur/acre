/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstLikeTmpEqualityExpr extends PTmpEqualityExpr
{
    private TIdentifier _identifier_;
    private TLike _like_;
    private PTmpRelationalExpr _tmpRelationalExpr_;

    public AFirstLikeTmpEqualityExpr()
    {
    }

    public AFirstLikeTmpEqualityExpr(
        TIdentifier _identifier_,
        TLike _like_,
        PTmpRelationalExpr _tmpRelationalExpr_)
    {
        setIdentifier(_identifier_);

        setLike(_like_);

        setTmpRelationalExpr(_tmpRelationalExpr_);

    }
    public Object clone()
    {
        return new AFirstLikeTmpEqualityExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TLike) cloneNode(_like_),
            (PTmpRelationalExpr) cloneNode(_tmpRelationalExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstLikeTmpEqualityExpr(this);
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

    public PTmpRelationalExpr getTmpRelationalExpr()
    {
        return _tmpRelationalExpr_;
    }

    public void setTmpRelationalExpr(PTmpRelationalExpr node)
    {
        if(_tmpRelationalExpr_ != null)
        {
            _tmpRelationalExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _tmpRelationalExpr_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_identifier_)
            + toString(_like_)
            + toString(_tmpRelationalExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_like_ == child)
        {
            _like_ = null;
            return;
        }

        if(_tmpRelationalExpr_ == child)
        {
            _tmpRelationalExpr_ = null;
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

        if(_like_ == oldChild)
        {
            setLike((TLike) newChild);
            return;
        }

        if(_tmpRelationalExpr_ == oldChild)
        {
            setTmpRelationalExpr((PTmpRelationalExpr) newChild);
            return;
        }

    }
}
