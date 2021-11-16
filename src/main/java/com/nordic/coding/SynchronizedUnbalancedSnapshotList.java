package com.nordic.coding;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Implementation class for SnapShotList
 * @author Sakthimurugan Elangovan
 *
 * @param <E>
 */
public class SynchronizedUnbalancedSnapshotList<E> extends AbstractList<E> implements SnapshotList<E> {

	/**
	* Default initial capacity.
	*/
	private static final int DEFAULT_CAPACITY = 10;
	
	
	/**
	* Shared empty array instance used for default sized empty instances. We
	* distinguish this from EMPTY_ELEMENTDATA to know how much to inflate when
	* first element is added.
	*/
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	
    /**
     * The maximum size of array to allocate.
     * Some VMs reserve some header words in an array.
     * Attempts to allocate larger arrays may result in
     * OutOfMemoryError: Requested array size exceeds VM limit
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
	
	/**
	* The array buffer into which the elements of the ArrayList are stored.
	* The capacity of the ArrayList is the length of this array buffer. Any
	* empty ArrayList with elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
	* will be expanded to DEFAULT_CAPACITY when the first element is added.
	*/
	Object[] elementData; // non-private to simplify nested class access
	
	private TreeMap<Integer,Object[]> snapShotMap;
	
    /**
     * The size of the CustomList (the number of elements it contains).
     *
     * @serial
     */
    private int size;
    
    /**
     * Version number of the SnapShot (the number of snapshots it contains).
     *
     * @serial
     */
    private int snapShotVersion = 0;
    
    /**
     * Constructs an empty list with an initial capacity of ten.
     */
	public SynchronizedUnbalancedSnapshotList(){
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
		this.snapShotMap = new TreeMap<>();
	}

   /**
    * Method to remove the snapshots based on the versions
    */
	@Override
	public synchronized void dropPriorSnapshots(int version) {
		if (version <= this.snapShotVersion){
			for(int i = snapShotMap.firstKey(); i < version; i++){
				snapShotMap.remove(i);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized E getAtVersion(int index, int version) {
		Object[] versionElements = snapShotMap.get(version);
		return (E)versionElements[index];
	}

	@Override
	public synchronized int snapshot() {
		snapShotVersion++;
		snapShotMap.put(snapShotVersion,Arrays.copyOf(this.elementData, elementData.length));
		return snapShotVersion;
	}

	@Override
	public int version() {
		return snapShotVersion;
	}

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public synchronized boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
    
    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their
     * indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public synchronized E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index,
                             numMoved);
        elementData[--size] = null; // clear to let GC do its work

        return oldValue;
    }
    
    /**
     * Replaces the element at the specified position in this list with
     * the specified element.
     *
     * @param index index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    public synchronized E set(int index, E element) {
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }
    
    /**
     * Calculate the capacity of array while doing add or remove
     * @param elementData
     * @param minCapacity
     * @return
     */
    private static int calculateCapacity(Object[] elementData, int minCapacity) {
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            return Math.max(DEFAULT_CAPACITY, minCapacity);
        }
        return minCapacity;
    }


    private synchronized void ensureCapacityInternal(int minCapacity) {
        ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
    }

    private synchronized void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }
    
    /**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        // minCapacity is usually close to size, so this is a win:
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    // Positional Access Operations

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }
    
	@Override
	public E get(int index) {
		rangeCheck(index);
		return elementData(index);
	}

    /**
     * Checks if the given index is in range.  If not, throws an appropriate
     * runtime exception.  This method does *not* check if the index is
     * negative: It is always used immediately prior to an array access,
     * which throws an ArrayIndexOutOfBoundsException if index is negative.
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
    
    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactoring of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }
    
   
	@Override
	public int size() {
		return size;
	}
	
    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
}
