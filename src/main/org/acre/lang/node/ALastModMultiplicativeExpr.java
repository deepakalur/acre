/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ALastModMultiplicativeExpr extends PMultiplicativeExpr
{
    private PMultiplicativeExpr _multiplicativeExpr_;
    private TMod _mod_;
    private TIdentifier _identifier_;

    public ALastModMultiplicativeExpr()
    {
    }

    public ALastModMultiplicativeExpr(
        PMultiplicativeExpr _multiplicativeExpr_,
        TMod _mod_,
        TIdentifier _identifier_)
    {
        setMultiplicativeExpr(_multiplicativeExpr_);

        setMod(_mod_);

        setIdentifier(_identifier_);

    }
    public Object clone()
    {
        return new ALastModMultiplicativeExpr(
            (PMultiplicativeExpr) cloneNode(_multiplicativeExpr_),
            (TMod) cloneNode(_mod_),
            (TIdentifier) cloneNode(_identifier_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseALastModMultiplicativeExpr(this);
    }

    public PMultiplicativeExpr getMultiplicativeExpr()
    {
        return _multiplicativeExpr_;
    }

    public void setMultiplicativeExpr(PMultiplicativeExpr node)
    {
        if(_multiplicativeExpr_ != null)
        {
            _multiplicativeExpr_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _multiplicativeExpr_ = node;
    }

    public TMod getMod()
    {
        return _mod_;
    }

    public void setMod(TMod node)
    {
        if(_mod_ != null)
        {
            _mod_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _mod_ = node;
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
            + toString(_multiplicativeExpr_)
            + toString(_mod_)
            + toString(_identifier_);
    }

    void removeChild(Node child)
    {
        if(_multiplicativeExpr_ == child)
        {
            _multiplicativeExpr_ = null;
            return;
        }

        if(_mod_ == child)
        {
            _mod_ = null;
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
        if(_multiplicativeExpr_ == oldChild)
        {
            setMultiplicativeExpr((PMultiplicativeExpr) newChild);
            return;
        }

        if(_mod_ == oldChild)
        {
            setMod((TMod) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

    }
}
