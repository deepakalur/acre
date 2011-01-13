/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ABagCollectionConstruction extends PCollectionConstruction
{
    private TBag _bag_;
    private TBracketL _bracketL_;
    private PValueList _valueList_;
    private TBracketR _bracketR_;

    public ABagCollectionConstruction()
    {
    }

    public ABagCollectionConstruction(
        TBag _bag_,
        TBracketL _bracketL_,
        PValueList _valueList_,
        TBracketR _bracketR_)
    {
        setBag(_bag_);

        setBracketL(_bracketL_);

        setValueList(_valueList_);

        setBracketR(_bracketR_);

    }
    public Object clone()
    {
        return new ABagCollectionConstruction(
            (TBag) cloneNode(_bag_),
            (TBracketL) cloneNode(_bracketL_),
            (PValueList) cloneNode(_valueList_),
            (TBracketR) cloneNode(_bracketR_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseABagCollectionConstruction(this);
    }

    public TBag getBag()
    {
        return _bag_;
    }

    public void setBag(TBag node)
    {
        if(_bag_ != null)
        {
            _bag_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _bag_ = node;
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

    public PValueList getValueList()
    {
        return _valueList_;
    }

    public void setValueList(PValueList node)
    {
        if(_valueList_ != null)
        {
            _valueList_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _valueList_ = node;
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
            + toString(_bag_)
            + toString(_bracketL_)
            + toString(_valueList_)
            + toString(_bracketR_);
    }

    void removeChild(Node child)
    {
        if(_bag_ == child)
        {
            _bag_ = null;
            return;
        }

        if(_bracketL_ == child)
        {
            _bracketL_ = null;
            return;
        }

        if(_valueList_ == child)
        {
            _valueList_ = null;
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
        if(_bag_ == oldChild)
        {
            setBag((TBag) newChild);
            return;
        }

        if(_bracketL_ == oldChild)
        {
            setBracketL((TBracketL) newChild);
            return;
        }

        if(_valueList_ == oldChild)
        {
            setValueList((PValueList) newChild);
            return;
        }

        if(_bracketR_ == oldChild)
        {
            setBracketR((TBracketR) newChild);
            return;
        }

    }
}
