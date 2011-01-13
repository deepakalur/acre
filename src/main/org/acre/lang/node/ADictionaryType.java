/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.acre.lang.node;

import java.util.*;
import org.acre.lang.analysis.*;

public final class ADictionaryType extends PType
{
    private TDictionary _dictionary_;
    private TLt _lt_;
    private PType _t1_;
    private TComma _comma_;
    private PType _t2_;
    private TGt _gt_;

    public ADictionaryType()
    {
    }

    public ADictionaryType(
        TDictionary _dictionary_,
        TLt _lt_,
        PType _t1_,
        TComma _comma_,
        PType _t2_,
        TGt _gt_)
    {
        setDictionary(_dictionary_);

        setLt(_lt_);

        setT1(_t1_);

        setComma(_comma_);

        setT2(_t2_);

        setGt(_gt_);

    }
    public Object clone()
    {
        return new ADictionaryType(
            (TDictionary) cloneNode(_dictionary_),
            (TLt) cloneNode(_lt_),
            (PType) cloneNode(_t1_),
            (TComma) cloneNode(_comma_),
            (PType) cloneNode(_t2_),
            (TGt) cloneNode(_gt_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseADictionaryType(this);
    }

    public TDictionary getDictionary()
    {
        return _dictionary_;
    }

    public void setDictionary(TDictionary node)
    {
        if(_dictionary_ != null)
        {
            _dictionary_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _dictionary_ = node;
    }

    public TLt getLt()
    {
        return _lt_;
    }

    public void setLt(TLt node)
    {
        if(_lt_ != null)
        {
            _lt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _lt_ = node;
    }

    public PType getT1()
    {
        return _t1_;
    }

    public void setT1(PType node)
    {
        if(_t1_ != null)
        {
            _t1_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _t1_ = node;
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

    public PType getT2()
    {
        return _t2_;
    }

    public void setT2(PType node)
    {
        if(_t2_ != null)
        {
            _t2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _t2_ = node;
    }

    public TGt getGt()
    {
        return _gt_;
    }

    public void setGt(TGt node)
    {
        if(_gt_ != null)
        {
            _gt_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        _gt_ = node;
    }

    public String toString()
    {
        return ""
            + toString(_dictionary_)
            + toString(_lt_)
            + toString(_t1_)
            + toString(_comma_)
            + toString(_t2_)
            + toString(_gt_);
    }

    void removeChild(Node child)
    {
        if(_dictionary_ == child)
        {
            _dictionary_ = null;
            return;
        }

        if(_lt_ == child)
        {
            _lt_ = null;
            return;
        }

        if(_t1_ == child)
        {
            _t1_ = null;
            return;
        }

        if(_comma_ == child)
        {
            _comma_ = null;
            return;
        }

        if(_t2_ == child)
        {
            _t2_ = null;
            return;
        }

        if(_gt_ == child)
        {
            _gt_ = null;
            return;
        }

    }

    void replaceChild(Node oldChild, Node newChild)
    {
        if(_dictionary_ == oldChild)
        {
            setDictionary((TDictionary) newChild);
            return;
        }

        if(_lt_ == oldChild)
        {
            setLt((TLt) newChild);
            return;
        }

        if(_t1_ == oldChild)
        {
            setT1((PType) newChild);
            return;
        }

        if(_comma_ == oldChild)
        {
            setComma((TComma) newChild);
            return;
        }

        if(_t2_ == oldChild)
        {
            setT2((PType) newChild);
            return;
        }

        if(_gt_ == oldChild)
        {
            setGt((TGt) newChild);
            return;
        }

    }
}