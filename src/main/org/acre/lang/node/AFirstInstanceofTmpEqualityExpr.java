/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstInstanceofTmpEqualityExpr extends PTmpEqualityExpr
{
    private TIdentifier _identifier_;
    private TInstanceof _instanceof_;
    private PTmpRelationalExpr _tmpRelationalExpr_;

    public AFirstInstanceofTmpEqualityExpr()
    {
    }

    public AFirstInstanceofTmpEqualityExpr(
        TIdentifier _identifier_,
        TInstanceof _instanceof_,
        PTmpRelationalExpr _tmpRelationalExpr_)
    {
        setIdentifier(_identifier_);

        setInstanceof(_instanceof_);

        setTmpRelationalExpr(_tmpRelationalExpr_);

    }
    public Object clone()
    {
        return new AFirstInstanceofTmpEqualityExpr(
            (TIdentifier) cloneNode(_identifier_),
            (TInstanceof) cloneNode(_instanceof_),
            (PTmpRelationalExpr) cloneNode(_tmpRelationalExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstInstanceofTmpEqualityExpr(this);
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
            + toString(_instanceof_)
            + toString(_tmpRelationalExpr_);
    }

    void removeChild(Node child)
    {
        if(_identifier_ == child)
        {
            _identifier_ = null;
            return;
        }

        if(_instanceof_ == child)
        {
            _instanceof_ = null;
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

        if(_instanceof_ == oldChild)
        {
            setInstanceof((TInstanceof) newChild);
            return;
        }

        if(_tmpRelationalExpr_ == oldChild)
        {
            setTmpRelationalExpr((PTmpRelationalExpr) newChild);
            return;
        }

    }
}