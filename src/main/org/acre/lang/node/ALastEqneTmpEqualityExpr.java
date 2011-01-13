/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastEqneTmpEqualityExpr extends PTmpEqualityExpr
{
    private PTmpEqualityExpr _tmpEqualityExpr_;
    private PEqne _eqne_;
    private PCompositePredicate _compositePredicate_;
    private TIdentifier _identifier_;

    public ALastEqneTmpEqualityExpr()
    {
    }

    public ALastEqneTmpEqualityExpr(
        PTmpEqualityExpr _tmpEqualityExpr_,
        PEqne _eqne_,
        PCompositePredicate _compositePredicate_,
        TIdentifier _identifier_)
    {
        setTmpEqualityExpr(_tmpEqualityExpr_);

        setEqne(_eqne_);

        setCompositePredicate(_compositePredicate_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastEqneTmpEqualityExpr(
            (PTmpEqualityExpr) cloneNode(_tmpEqualityExpr_),
            (PEqne) cloneNode(_eqne_),
            (PCompositePredicate) cloneNode(_compositePredicate_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastEqneTmpEqualityExpr(this);
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

    public PEqne getEqne()
    {
        return _eqne_;
    }

    public void setEqne(PEqne node)
    {
        if(_eqne_ != null)
        {
            _eqne_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _eqne_ = node;
    }

    public PCompositePredicate getCompositePredicate()
    {
        return _compositePredicate_;
    }

    public void setCompositePredicate(PCompositePredicate node)
    {
        if(_compositePredicate_ != null)
        {
            _compositePredicate_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _compositePredicate_ = node;
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
            + toString(_eqne_)
            + toString(_compositePredicate_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_tmpEqualityExpr_ == child)
        {
            _tmpEqualityExpr_ = null;
            return;
        }

        if(_eqne_ == child)
        {
            _eqne_ = null;
            return;
        }

        if(_compositePredicate_ == child)
        {
            _compositePredicate_ = null;
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

        if(_eqne_ == oldChild)
        {
            setEqne((PEqne) newChild);
            return;
        }

        if(_compositePredicate_ == oldChild)
        {
            setCompositePredicate((PCompositePredicate) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}