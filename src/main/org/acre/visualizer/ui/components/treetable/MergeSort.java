/**
 *   Copyright 2004-2005 Sun Microsystems, Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.acre.visualizer.ui.components.treetable;

/**
 * An implementation of MergeSort, needs to be subclassed to
 * compare the terms.
 *
 * @author Scott Violet
 */
public abstract class MergeSort extends Object {
    protected Object           toSort[];
    protected Object           swapSpace[];

    public void sort(Object array[]) {
	if(array != null && array.length > 1)
	{
	    int             maxLength;
  
	    maxLength = array.length;
	    swapSpace = new Object[maxLength];
	    toSort = array;
	    this.mergeSort(0, maxLength - 1);
	    swapSpace = null;
	    toSort = null;
	}
    }

    public abstract int compareElementsAt(int beginLoc, int endLoc);

    protected void mergeSort(int begin, int end) {
	if(begin != end)
	{
	    int           mid;

	    mid = (begin + end) / 2;
	    this.mergeSort(begin, mid);
	    this.mergeSort(mid + 1, end);
	    this.merge(begin, mid, end);
	}
    }

    protected void merge(int begin, int middle, int end) {
	int           firstHalf, secondHalf, count;

	firstHalf = count = begin;
	secondHalf = middle + 1;
	while((firstHalf <= middle) && (secondHalf <= end))
	{
	    if(this.compareElementsAt(secondHalf, firstHalf) < 0)
		swapSpace[count++] = toSort[secondHalf++];
	    else
		swapSpace[count++] = toSort[firstHalf++];
	}
	if(firstHalf <= middle)
	{
	    while(firstHalf <= middle)
		swapSpace[count++] = toSort[firstHalf++];
	}
	else
	{
	    while(secondHalf <= end)
		swapSpace[count++] = toSort[secondHalf++];
	}
	for(count = begin;count <= end;count++)
	    toSort[count] = swapSpace[count];
    }
}
