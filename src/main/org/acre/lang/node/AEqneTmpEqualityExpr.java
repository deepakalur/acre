/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AEqneTmpEqualityExpr extends PTmpEqualityExpr
{
    private PTmpEqualityExpr _tmpEqualityExpr_;
    private PEqne _eqne_;
    private PCompositePredicate _compositePredicate_;
    private PTmpRelationalExpr _tmpRelationalExpr_;

    public AEqneTmpEqualityExpr()
    {
    }

    public AEqneTmpEqualityExpr(
        PTmpEqualityExpr _tmpEqualityExpr_,
        PEqne _eqne_,
        PCompositePredicate _compositePredicate_,
        PTmpRelationalExpr _tmpRelationalExpr_)
    {
        setTmpEqualityExpr(_tmpEqualityExpr_);

        setEqne(_eqne_);

        setCompositePredicate(_compositePredicate_);

        setTmpRelationalExpr(_tmpRelationalExpr_);

    }
    public Object clone()
    {
        return new AEqneTmpEqualityExpr(
            (PTmpEqualityExpr) cloneNode(_tmpEqualityExpr_),
            (PEqne) cloneNode(_eqne_),
            (PCompositePredicate) cloneNode(_compositePredicate_),
            (PTmpRelationalExpr) cloneNode(_tmpRelationalExpr_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAEqneTmpEqualityExpr(this);
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
            + toString(_tmpEqualityExpr_)
            + toString(_eqne_)
            + toString(_compositePredicate_)
            + toString(_tmpRelationalExpr_);
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

        if(_tmpRelationalExpr_ == child)
        {
            _tmpRelationalExpr_ = null;
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

        if(_tmpRelationalExpr_ == oldChild)
        {
            setTmpRelationalExpr((PTmpRelationalExpr) newChild);
            return;
        }

    }
}