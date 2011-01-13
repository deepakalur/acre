/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AFirstDistinctIdentifierTmpEqualityExpr extends PTmpEqualityExpr
{
    private TIdentifier _left_;
    private PEqne _eqne_;
    private PCompositePredicate _compositePredicate_;
    private TDistinct _distinct_;
    private TBracketL _bracketL_;
    private TIdentifier _right_;
    private TBracketR _bracketR_;

    public AFirstDistinctIdentifierTmpEqualityExpr()
    {
    }

    public AFirstDistinctIdentifierTmpEqualityExpr(
        TIdentifier _left_,
        PEqne _eqne_,
        PCompositePredicate _compositePredicate_,
        TDistinct _distinct_,
        TBracketL _bracketL_,
        TIdentifier _right_,
        TBracketR _bracketR_)
    {
        setLeft(_left_);

        setEqne(_eqne_);

        setCompositePredicate(_compositePredicate_);

        setDistinct(_distinct_);

        setBracketL(_bracketL_);

        setRight(_right_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AFirstDistinctIdentifierTmpEqualityExpr(
            (TIdentifier) cloneNode(_left_),
            (PEqne) cloneNode(_eqne_),
            (PCompositePredicate) cloneNode(_compositePredicate_),
            (TDistinct) cloneNode(_distinct_),
            (TBracketL) cloneNode(_bracketL_),
            (TIdentifier) cloneNode(_right_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFirstDistinctIdentifierTmpEqualityExpr(this);
    }

    public TIdentifier getLeft()
    {
        return _left_;
    }

    public void setLeft(TIdentifier node)
    {
        if(_left_ != null)
        {
            _left_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _left_ = node;
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

    public TDistinct getDistinct()
    {
        return _distinct_;
    }

    public void setDistinct(TDistinct node)
    {
        if(_distinct_ != null)
        {
            _distinct_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _distinct_ = node;
    }

    public TBracketL getBracketL()
    {
        return _bracketL_;
    }

    public void setBracketL(TBracketL node)
    {
        if(_bracketL_ != null)
        {
            _bracketL_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketL_ = node;
    }

    public TIdentifier getRight()
    {
        return _right_;
    }

    public void setRight(TIdentifier node)
    {
        if(_right_ != null)
        {
            _right_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _right_ = node;
    }

    public TBracketR getBracketR()
    {
        return _bracketR_;
    }

    public void setBracketR(TBracketR node)
    {
        if(_bracketR_ != null)
        {
            _bracketR_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bracketR_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_left_)
            + toString(_eqne_)
            + toString(_compositePredicate_)
            + toString(_distinct_)
            + toString(_bracketL_)
            + toString(_right_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_left_ == child)
        {
            _left_ = null;
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

        if(_distinct_ == child)
        {
            _distinct_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_right_ == child)
        {
            _right_ = null;
            return;
        }

        if(_bracketR_ == child)
        {
            _bracketR_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_left_ == oldChild)
        {
            setLeft((TIdentifier) newChild);
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

        if(_distinct_ == oldChild)
        {
            setDistinct((TDistinct) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_right_ == oldChild)
        {
            setRight((TIdentifier) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
