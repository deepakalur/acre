/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class AMultipleBracketedTupleList extends PBracketedTupleList
{
    private TReturn _return_;
    private TBracketL _bracketL_;
    private TIdentifier _identifier_;
    private TComma _comma_;
    private PIdentifierList _identifierList_;
    private TBracketR _bracketR_;

    public AMultipleBracketedTupleList()
    {
    }

    public AMultipleBracketedTupleList(
        TReturn _return_,
        TBracketL _bracketL_,
        TIdentifier _identifier_,
        TComma _comma_,
        PIdentifierList _identifierList_,
        TBracketR _bracketR_)
    {
        setReturn(_return_);

        setBracketL(_bracketL_);

        setIdentifier(_identifier_);

        setComma(_comma_);

        setIdentifierList(_identifierList_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new AMultipleBracketedTupleList(
            (TReturn) cloneNode(_return_),
            (TBracketL) cloneNode(_bracketL_),
            (TIdentifier) cloneNode(_identifier_),
            (TComma) cloneNode(_comma_),
            (PIdentifierList) cloneNode(_identifierList_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMultipleBracketedTupleList(this);
    }

    public TReturn getReturn()
    {
        return _return_;
    }

    public void setReturn(TReturn node)
    {
        if(_return_ != null)
        {
            _return_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _return_ = node;
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

    public TComma getComma()
    {
        return _comma_;
    }

    public void setComma(TComma node)
    {
        if(_comma_ != null)
        {
            _comma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _comma_ = node;
    }

    public PIdentifierList getIdentifierList()
    {
        return _identifierList_;
    }

    public void setIdentifierList(PIdentifierList node)
    {
        if(_identifierList_ != null)
        {
            _identifierList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _identifierList_ = node;
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
            + toString(_return_)
            + toString(_bracketL_)
            + toString(_identifier_)
            + toString(_comma_)
            + toString(_identifierList_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_return_ == child)
        {
            _return_ = null;
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

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_identifierList_ == child)
        {
            _identifierList_ = null;
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
        if(_return_ == oldChild)
        {
            setReturn((TReturn) newChild);
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

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_identifierList_ == oldChild)
        {
            setIdentifierList((PIdentifierList) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
