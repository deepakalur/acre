/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastInstanceofTmpEqualityExpr extends PTmpEqualityExpr
{
    private PTmpEqualityExpr _tmpEqualityExpr_;
    private TInstanceof _instanceof_;
    private TIdentifier _identifier_;

    public ALastInstanceofTmpEqualityExpr()
    {
    }

    public ALastInstanceofTmpEqualityExpr(
        PTmpEqualityExpr _tmpEqualityExpr_,
        TInstanceof _instanceof_,
        TIdentifier _identifier_)
    {
        setTmpEqualityExpr(_tmpEqualityExpr_);

        setInstanceof(_instanceof_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastInstanceofTmpEqualityExpr(
            (PTmpEqualityExpr) cloneNode(_tmpEqualityExpr_),
            (TInstanceof) cloneNode(_instanceof_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastInstanceofTmpEqualityExpr(this);
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

    public TInstanceof getInstanceof()
    {
        return _instanceof_;
    }

    public void setInstanceof(TInstanceof node)
    {
        if(_instanceof_ != null)
        {
            _instanceof_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _instanceof_ = node;
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
            + toString(_instanceof_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpEqualityExpr_ == child)
        {
            _tmpEqualityExpr_ = null;
            return;
        }

        if(_instanceof_ == child)
        {
            _instanceof_ = null;
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

        if(_instanceof_ == oldChild)
        {
            setInstanceof((TInstanceof) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}
