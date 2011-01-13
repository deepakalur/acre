/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AIdentifierExistsCollectionExpr extends PCollectionExpr
{
    private TExists _exists_;
    private TBracketL _bracketL_;
    private TIdentifier _identifier_;
    private TBracketR _bracketR_;

    public AIdentifierExistsCollectionExpr()
    {
    }

    public AIdentifierExistsCollectionExpr(
        TExists _exists_,
        TBracketL _bracketL_,
        TIdentifier _identifier_,
        TBracketR _bracketR_)
    {
        setExists(_exists_);

        setBracketL(_bracketL_);

        setIdentifier(_identifier_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AIdentifierExistsCollectionExpr(
            (TExists) cloneNode(_exists_),
            (TBracketL) cloneNode(_bracketL_),
            (TIdentifier) cloneNode(_identifier_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAIdentifierExistsCollectionExpr(this);
    }

    public TExists getExists()
    {
        return _exists_;
    }

    public void setExists(TExists node)
    {
        if(_exists_ != null)
        {
            _exists_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _exists_ = node;
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
            + toString(_exists_)
            + toString(_bracketL_)
            + toString(_identifier_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_exists_ == child)
        {
            _exists_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_identifier_ == child)
        {
            _identifier_ = null;
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
        if(_exists_ == oldChild)
        {
            setExists((TExists) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_identifier_ == oldChild)
        {
            setIdentifier((TIdentifier) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
