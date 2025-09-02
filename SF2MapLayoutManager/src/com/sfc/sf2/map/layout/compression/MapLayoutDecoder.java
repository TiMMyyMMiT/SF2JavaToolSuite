/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package com.sfc.sf2.map.layout.compression;

import com.sfc.sf2.core.gui.controls.Console;
import com.sfc.sf2.helpers.BinaryHelpers;
import com.sfc.sf2.map.block.MapBlock;
import com.sfc.sf2.map.block.MapBlockset;
import com.sfc.sf2.map.layout.MapLayout;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TiMMy
 */
public class MapLayoutDecoder {

    private static final int COMMAND_LEFTMAP = 0;
    private static final int COMMAND_UPPERMAP = 1;
    private static final int COMMAND_CUSTOMVALUE = 2;

    private byte[] inputData;
    private short inputWord = 0;
    private int inputCursor = -2;
    private int inputBitCursor = 16;

    private int blocksetCursor;
    private int blockCursor;

    MapBlock[][] leftHistoryMap = null;
    MapBlock[][] upperHistoryMap = null;
    
    //private StringBuilder debugSb = null;

    public MapLayout decode(byte[] layoutData, MapBlock[] blockset) {
        inputData = layoutData;
        MapLayout layout = new MapLayout();
        MapBlock[] blocks = new MapBlock[64 * 64];
        leftHistoryMap = new MapBlock[blockset.length][4];
        upperHistoryMap = new MapBlock[blockset.length][4];
        blocksetCursor = 2;
        blockCursor = 0;
        //debugSb = new StringBuilder();
        while (blockCursor < 64 * 64) {
            //debugSb.append(" ");
            MapBlock block = null;
            if (getNextBit() == 0) {
                if (getNextBit() == 0) {
                    /* 00 */
                    //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 00 : Output next block from block set.");
                    blocksetCursor++;
                    block = blockset[blocksetCursor].clone();
                    applyFlags(block);
                    blocks[blockCursor] = block;
                    if (blockCursor > 0) {
                        saveBlockToLeftStackMap(blocks[blockCursor-1].getIndex(), block);
                    } else {
                        saveBlockToLeftStackMap(0, block);
                    }
                    if (blockCursor >= MapLayout.BLOCK_WIDTH) {
                        saveBlockToUpperStackMap(blocks[blockCursor-MapLayout.BLOCK_WIDTH].getIndex(), block);
                    } else {
                        saveBlockToUpperStackMap(0, block);
                    }
                    blockCursor++;
                } else {
                    /* 01 */
                    //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 01 : Copy section.");
                    int count = 0;
                    while (getNextBit() == 0) {
                        count++;
                    }
                    int cursor = count;
                    int value = 0;
                    while (cursor > 0) {
                        value = value*2+getNextBit();
                        cursor--;
                    }
                    int result = value + (1<<count);
                    //Console.logger().finest(" count="+count+", value="+value+", result="+result);
                    int offset = (getNextBit() == 1) ? 1 : MapLayout.BLOCK_WIDTH;
                    for (int i = 0; i < result; i++) {
                        if (blockCursor < MapLayout.BLOCK_COUNT) {
                            MapBlock source = blocks[blockCursor-offset];
                            MapBlock copy = source.clone();
                            blocks[blockCursor] = copy;
                            //Console.logger().finest(" Copy of block=$" + Integer.toHexString(blocks[blockCursor].getIndex())+" / "+blocks[blockCursor].getIndex());
                            blockCursor++;
                        }
                    }
                    //Console.logger().finest(debugSb.substring(debugSb.length()-1-2));
                    //Console.logger().finest("outputData="+outputData);
                }
            } else {
                /* 1... check if left block history stack available */
                int commandType;
                int leftBlockCursor = (blockCursor > 0) ? blocks[blockCursor-1].getIndex() : 0;
                int upperBlockCursor = (blockCursor >= MapLayout.BLOCK_WIDTH) ? blocks[blockCursor-MapLayout.BLOCK_WIDTH].getIndex() : 0;
                if (leftHistoryMap[leftBlockCursor][0] != null) {
                    /* 1... left block stack available, check next bit */
                    if (getNextBit() == 0) {
                        /* 10 : Get block from left block history map */
                        //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 10 : Get block from left block history map.");
                        commandType = COMMAND_LEFTMAP;
                    } else {
                        /* 11... check if upper block history stack available */
                        if (upperHistoryMap[upperBlockCursor][0] != null) {
                            /* 11... check next bit */
                            if (getNextBit() == 0) {
                                /* 110 : Get block from upper block history map */
                                //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 110 : Get block from upper block history map.");
                                commandType = COMMAND_UPPERMAP;
                            } else {
                                /* 111 : custom value */
                                //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 111 : Custom value.");
                                commandType = COMMAND_CUSTOMVALUE;
                            }
                        } else {
                            /* 11 with no upper stack : Custom value */
                            //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 11 with no upper stack : Custom value.");
                            commandType = COMMAND_CUSTOMVALUE;
                        }
                    }
                } else {
                    /* 1... check if upper block history stack available */
                    if (upperHistoryMap[upperBlockCursor][0] != null) {
                        /* 1... check next bit */
                        if (getNextBit() == 0) {
                            /* 10 with no left stack : Get block from upper block history map */
                            //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 10 with no left stack : Get block from upper block history map.");
                            commandType = COMMAND_UPPERMAP;
                        } else {
                            /* 11 with no left stack : custom value */
                            //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 11 with no left stack : Custom value.");
                            commandType = COMMAND_CUSTOMVALUE;
                        }
                    } else {
                        /* 1 with no left and upper stack : Custom value*/
                        //Console.logger().finest("Block=$" + Integer.toHexString(blockCursor)+" - 1 with no left or upper stack : Custom value.");
                        commandType = COMMAND_CUSTOMVALUE;
                    }
                }
                int stackSize = 0;
                int stackTarget = 0;
                MapBlock targetBlock = null;
                switch (commandType) {

                    case COMMAND_LEFTMAP:
                        if (leftHistoryMap[leftBlockCursor][1] == null) {
                            targetBlock = leftHistoryMap[leftBlockCursor][0];
                            //Console.logger().finest(" Stack contains only one entry : get entry 0.");
                        } else {
                            for (int i = 0; i < 4; i++) {
                                if (leftHistoryMap[leftBlockCursor][i] != null) {
                                    stackSize++;
                                }
                            }
                            while (stackSize > 1) {
                                stackSize--;
                                if (getNextBit() == 0) {
                                    stackTarget++;
                                } else {
                                    break;
                                }
                            }
                            //Console.logger().finest(" Get stack entry "+stackTarget);
                            targetBlock = leftHistoryMap[leftBlockCursor][stackTarget];
                        }
                        block = targetBlock.clone();
                        break;

                    case COMMAND_UPPERMAP:
                        if (upperHistoryMap[upperBlockCursor][1] == null) {
                            targetBlock = upperHistoryMap[upperBlockCursor][0];
                            //Console.logger().finest(" Stack contains only one entry : get entry 0.");
                        } else {
                            for (int i = 0; i < 4; i++) {
                                if (upperHistoryMap[upperBlockCursor][i] != null) {
                                    stackSize++;
                                }
                            }
                            while (stackSize > 1) {
                                stackSize--;
                                if (getNextBit() == 0) {
                                    stackTarget++;
                                } else {
                                    break;
                                }
                            }
                            //Console.logger().finest(" Get stack entry "+stackTarget);
                            targetBlock = upperHistoryMap[upperBlockCursor][stackTarget];
                        }
                        block = targetBlock.clone();
                        break;

                    case COMMAND_CUSTOMVALUE:
                        int length = Integer.toString(blocksetCursor, 2).length();
                        int value = 0;
                        while (length > 0) {
                            value = value*2+getNextBit();
                            length--;
                        }
                        //Console.logger().finest(" blocksetCursor=="+blocksetCursor+" / "+Integer.toString(blocksetCursor,2)+", length="+Integer.toString(blocksetCursor,2).length()+", Value="+value);
                        targetBlock = blockset[value];
                        block = targetBlock.clone();
                        applyFlags(block);
                        break;
                }

                if (block == null) {
                    //block = MapBlock.EmptyMapBlock(-1, 0, palette);
                }
                blocks[blockCursor] = block;

                if (blockCursor > 0) {
                    saveBlockToLeftStackMap(blocks[blockCursor-1].getIndex(), block);
                } else {
                    saveBlockToLeftStackMap(0, block);
                }
                if (blockCursor >= MapLayout.BLOCK_WIDTH) {
                    saveBlockToUpperStackMap(blocks[blockCursor-MapLayout.BLOCK_WIDTH].getIndex(), block);
                } else {
                    saveBlockToUpperStackMap(0, block);
                }

                blockCursor++;
            }

            //Console.logger().finest(" Output block = $" + Integer.toHexString(block.getIndex())+" / "+block.getIndex());
            //Console.logger().finest(debugSb.substring(debugSb.lastIndexOf(" ")));  
        }
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] == null) {
                blocks[i] = blockset[0];
            }
        }
        MapBlockset layoutBlockset = new MapBlockset(blocks, MapLayout.BLOCK_WIDTH);
        layout.setBlockset(layoutBlockset);
        return layout;
    }

    private void applyFlags(MapBlock block) {
        short flags = 0;
        if (getNextBit() == 0) {
            if (getNextBit() == 0) {
                /* 00 : no flag set */
            } else {
                /* 01 : $C000*/
                flags = (short)0xC000;
            }
        } else {
            if (getNextBit() == 0) {
                if (getNextBit() == 0) {
                    /* 100 : $4000 */
                    flags = (short)0x4000;
                } else {
                    /* 101 : $8000 */
                    flags = (short)0x8000;
                }
            } else {
                /* 11 : next 6 bits = flag mask XXXX XX00 0000 0000 */
                flags = (short) (getNextBit() * 0x8000
                        + getNextBit() * 0x4000
                        + getNextBit() * 0x2000
                        + getNextBit() * 0x1000
                        + getNextBit() * 0x0800
                        + getNextBit() * 0x0400);
            }
        }
        block.setFlags(flags & 0xFFFF);
    }

    private void saveBlockToLeftStackMap(int leftBlockIndex, MapBlock block) {
        MapBlock[] currentStack = leftHistoryMap[leftBlockIndex];

        if (!block.equalsIgnoreTiles(currentStack[0])) {
            MapBlock[] newStack = new MapBlock[4];
            leftHistoryMap[leftBlockIndex] = newStack;
            newStack[0] = block;
            int currentStackCursor = 0;
            int newStackCursor = 1;
            while (newStackCursor < 4) {
                if (currentStack[currentStackCursor] != null) {
                    if (!block.equalsIgnoreTiles(currentStack[currentStackCursor])) {
                        newStack[newStackCursor] = currentStack[currentStackCursor];
                        currentStackCursor++;
                        newStackCursor++;
                    } else {
                        currentStackCursor++;
                    }
                } else {
                    return;
                }
            }
        }
    }

    private void saveBlockToUpperStackMap(int upperBlockIndex, MapBlock block) {
        MapBlock[] currentStack = upperHistoryMap[upperBlockIndex];

        if (!block.equalsIgnoreTiles(currentStack[0])) {
            MapBlock[] newStack = new MapBlock[4];
            upperHistoryMap[upperBlockIndex] = newStack;
            newStack[0] = block;
            int currentStackCursor = 0;
            int newStackCursor = 1;
            while (newStackCursor < 4) {
                if (currentStack[currentStackCursor] != null) {
                    if (!block.equalsIgnoreTiles(currentStack[currentStackCursor])) {
                        newStack[newStackCursor] = currentStack[currentStackCursor];
                        currentStackCursor++;
                        newStackCursor++;
                    } else {
                        currentStackCursor++;
                    }
                } else {
                    return;
                }
            }
        }
    }

    private int getNextBit() {
        int bit = 0;
        if (inputBitCursor >= 16) {
            inputBitCursor = 0;
            inputCursor += 2;
            inputWord = BinaryHelpers.getWord(inputData, inputCursor);
        }
        bit = (inputWord >> (15 - inputBitCursor)) & 1;
        inputBitCursor++;
        //debugSb.append(bit);
        return bit;
    }

    public MapBlock[] encodeNewBlockset(MapBlock[] blockSet, MapLayout layout) {
        List<Integer> newBlocksetValues = new ArrayList<>();
        MapBlock[] newBlockset;
        MapBlock[] blocks = layout.getBlockset().getBlocks();
        /* Add base blocks : empty, closed chest and open chest */
        newBlocksetValues.add(blockSet[0].getIndex());
        newBlocksetValues.add(blockSet[1].getIndex());
        newBlocksetValues.add(blockSet[2].getIndex());
        /* Add blocks in layout's appearing order */
        for (int i = 0; i < blocks.length; i++) {
            if (!newBlocksetValues.contains(blocks[i].getIndex())) {
                newBlocksetValues.add(blocks[i].getIndex());
            }
        }
        /* Add remaining unused blocks */
        for (int i = 0; i < blockSet.length; i++) {
            if (!newBlocksetValues.contains(blockSet[i].getIndex())) {
                newBlocksetValues.add(blockSet[i].getIndex());
            }
        }
        newBlockset = new MapBlock[newBlocksetValues.size()];
        for (int i = 0; i < newBlockset.length; i++) {
            newBlockset[i] = blockSet[newBlocksetValues.get(i)];
        }
        for (int i = 0; i < blocks.length; i++) {
            MapBlock block = blocks[i];
            for (int j = 0; j < newBlockset.length; j++) {
                if (block.getIndex() == newBlockset[j].getIndex()) {
                    block.setIndex(j);
                    break;
                }
            }
        }
        for (int i = 0; i < newBlockset.length; i++) {
            newBlockset[i].setIndex(i);
        }
        return newBlockset;
    }

    public byte[] encode(MapLayout layout, MapBlock[] blockset) {
        leftHistoryMap = new MapBlock[blockset.length][4];
        upperHistoryMap = new MapBlock[blockset.length][4];

        StringBuilder outputSb = new StringBuilder();
        outputSb.append(" ");
        byte[] output;
        blocksetCursor = 3;
        blockCursor = 0;

        String leftCopyCandidate;
        int leftCopyLength;
        String upperCopyCandidate;
        int upperCopyLength;
        String leftHistoryCandidate;
        String upperHistoryCandidate;
        String nextBlockCandidate;
        String customBlockCandidate;

        MapBlock[] blocks = layout.getBlockset().getBlocks();

        while (blockCursor < 64 * 64) {

            leftCopyCandidate = null;
            leftCopyLength = 0;
            upperCopyCandidate = null;
            upperCopyLength = 0;
            leftHistoryCandidate = null;
            upperHistoryCandidate = null;
            nextBlockCandidate = null;
            customBlockCandidate = null;

            MapBlock block = blocks[blockCursor];
            //Console.logger().finest("Block $"+Integer.toString(block.getIndex(),16)+" / $"+Integer.toString(block.getFlags(),16));
            MapBlock leftBlock = null;
            int leftHistoryCursor = 0;
            if (blockCursor > 0) {
                leftBlock = blocks[blockCursor - 1];
                leftHistoryCursor = leftBlock.getIndex();
            }
            MapBlock upperBlock = null;
            int upperHistoryCursor = 0;
            if (blockCursor > 63) {
                upperBlock = blocks[blockCursor - 64];
                upperHistoryCursor = upperBlock.getIndex();
            }

            /* Produce candidate commands */
            if (block.equalsIgnoreTiles(leftBlock)) {
                /* Produce leftCopyCandidate with length */
                leftCopyLength = 1;
                while (blockCursor + leftCopyLength < blocks.length && blocks[blockCursor + leftCopyLength].equalsIgnoreTiles(blocks[blockCursor - 1 + leftCopyLength])) {
                    leftCopyLength++;
                }
                int powerOfTwo = 0;
                while ((1 << powerOfTwo) <= leftCopyLength) {
                    powerOfTwo++;
                }
                powerOfTwo--;
                int rest = leftCopyLength - (1 << powerOfTwo);
                StringBuilder commandSb = new StringBuilder();
                commandSb.append("01");
                int zeros = powerOfTwo;
                while (zeros > 0) {
                    commandSb.append("0");
                    zeros--;
                }
                commandSb.append("1");
                if (powerOfTwo > 0) {
                    String restString = Integer.toString(rest, 2);
                    while (restString.length() < powerOfTwo) {
                        restString = "0" + restString;
                    }
                    commandSb.append(restString);

                }
                commandSb.append("1");
                leftCopyCandidate = commandSb.toString();
                //Console.logger().finest(" leftCopyCandidate="+leftCopyCandidate+" - "+leftCopyLength+" blocks");
            }

            if (block.equalsIgnoreTiles(upperBlock)) {
                /* Produce upperCopyCandidate with length */
                upperCopyLength = 1;
                while (blockCursor + upperCopyLength < blocks.length && blocks[blockCursor + upperCopyLength].equalsIgnoreTiles(blocks[blockCursor - 64 + upperCopyLength])) {
                    upperCopyLength++;
                }
                int powerOfTwo = 0;
                while ((1 << powerOfTwo) <= upperCopyLength) {
                    powerOfTwo++;
                }
                powerOfTwo--;
                int rest = upperCopyLength - (1 << powerOfTwo);
                StringBuilder commandSb = new StringBuilder();
                commandSb.append("01");
                int zeros = powerOfTwo;
                while (zeros > 0) {
                    commandSb.append("0");
                    zeros--;
                }
                commandSb.append("1");
                if (powerOfTwo > 0) {
                    String restString = Integer.toString(rest, 2);
                    while (restString.length() < powerOfTwo) {
                        restString = "0" + restString;
                    }
                    commandSb.append(restString);
                }
                commandSb.append("0");
                upperCopyCandidate = commandSb.toString();
                //Console.logger().finest(" upperCopyCandidate="+upperCopyCandidate+" - "+upperCopyLength+" blocks");
            }

            int leftBlockHistoryIndex = getLeftHistoryIndex(leftHistoryCursor, block);
            if (leftBlockHistoryIndex >= 0) {
                /* Produce leftHistoryCandidate*/
                StringBuilder commandSb = new StringBuilder();
                commandSb.append("10");
                MapBlock[] stack = leftHistoryMap[leftHistoryCursor];
                if (stack[1] == null) {
                    /* No index to add */
                } else {
                    int stackSize = 0;
                    for (int i = 0; i < 4; i++) {
                        if (stack[i] != null) {
                            stackSize++;
                        }
                    }
                    for (int i = 0; i <= stackSize; i++) {
                        if (block.equalsIgnoreTiles(stack[i])) {
                            if (i < stackSize - 1) {
                                commandSb.append("1");
                            }
                            break;
                        } else {
                            commandSb.append("0");
                        }
                    }
                }
                leftHistoryCandidate = commandSb.toString();
                //Console.logger().finest(" leftHistoryCandidate="+leftHistoryCandidate);
            }

            int upperBlockHistoryIndex = getUpperHistoryIndex(upperHistoryCursor, block);
            if (upperBlockHistoryIndex >= 0) {
                /* Produce upperHistoryCandidate*/
                StringBuilder commandSb = new StringBuilder();
                commandSb.append("10");
                MapBlock[] stack = upperHistoryMap[upperHistoryCursor];
                if (stack[1] == null) {
                    /* No index to add */
                } else {
                    int stackSize = 0;
                    for (int i = 0; i < 4; i++) {
                        if (stack[i] != null) {
                            stackSize++;
                        }
                    }
                    for (int i = 0; i <= stackSize; i++) {
                        if (block.equalsIgnoreTiles(stack[i])) {
                            if (i < stackSize - 1) {
                                commandSb.append("1");
                            }
                            break;
                        } else {
                            commandSb.append("0");
                        }
                    }
                }
                if (leftHistoryMap[leftHistoryCursor][0] != null) {
                    commandSb.insert(0, "1");
                }
                upperHistoryCandidate = commandSb.toString();
                //Console.logger().finest(" upperHistoryCandidate="+upperHistoryCandidate);
            }

            if (leftCopyCandidate == null && upperCopyCandidate == null && leftHistoryCandidate == null && upperHistoryCandidate == null) {
                if (blocksetCursor < blockset.length && block.getIndex() == blockset[blocksetCursor].getIndex()) {
                    /* Produce nextBlockCandidate */
                    nextBlockCandidate = "00" + produceFlagBits(block.getFlags());
                    //Console.logger().finest(" nextBlockCandidate="+nextBlockCandidate);
                }

                if (nextBlockCandidate == null) {
                    /* Produce customBlockCandidate */
                    StringBuilder commandSb = new StringBuilder();
                    commandSb.append("1");
                    int length = Integer.toString(blocksetCursor - 1, 2).length();
                    //Console.logger().finest(" blocksetCursor="+(blocksetCursor-1)+" / "+Integer.toString(blocksetCursor-1,2)+", length="+length);
                    String value = Integer.toString(block.getIndex(), 2);
                    while (value.length() < length) {
                        value = "0" + value;
                    }
                    commandSb.append(value);
                    commandSb.append(produceFlagBits(block.getFlags()));
                    if (leftHistoryMap[leftHistoryCursor][0] != null && upperHistoryMap[upperHistoryCursor][0] != null) {
                        commandSb.insert(0, "11");
                    } else if (leftHistoryMap[leftHistoryCursor][0] != null || upperHistoryMap[upperHistoryCursor][0] != null) {
                        commandSb.insert(0, "1");
                    }
                    customBlockCandidate = commandSb.toString();
                    //Console.logger().finest(" customBlockCandidate="+customBlockCandidate);
                }

            }

            /* Select command to output */
            if (leftCopyLength > 1 || upperCopyLength > 1) {
                if (leftCopyLength > upperCopyLength) {
                    outputSb.append(leftCopyCandidate);
                    blockCursor += leftCopyLength;
                } else {
                    outputSb.append(upperCopyCandidate);
                    blockCursor += upperCopyLength;
                }
            } else {
                if (nextBlockCandidate != null) {
                    outputSb.append(nextBlockCandidate);
                    saveHistoryMaps(leftBlock, upperBlock, blockCursor, block);
                    blockCursor++;
                    blocksetCursor++;
                } else if (upperCopyCandidate != null) {
                    outputSb.append(upperCopyCandidate);
                    blockCursor += upperCopyLength;
                } else if (leftCopyCandidate != null) {
                    outputSb.append(leftCopyCandidate);
                    blockCursor += leftCopyLength;
                } else if (leftHistoryCandidate != null) {
                    outputSb.append(leftHistoryCandidate);
                    saveHistoryMaps(leftBlock, upperBlock, blockCursor, block);
                    blockCursor++;
                } else if (upperHistoryCandidate != null) {
                    outputSb.append(upperHistoryCandidate);
                    saveHistoryMaps(leftBlock, upperBlock, blockCursor, block);
                    blockCursor++;
                } else if (customBlockCandidate != null) {
                    outputSb.append(customBlockCandidate);
                    saveHistoryMaps(leftBlock, upperBlock, blockCursor, block);
                    blockCursor++;
                } else {
                    Console.logger().finest("ERROR : NO CANDIDATE COMMAND FOUND FOR BLOCK.");
                }

            }

            //Console.logger().finest(" Selected command ="+outputSb.substring(outputSb.lastIndexOf(" ")));     
            outputSb.append(" ");

        }

        //Console.logger().finest("output = " + outputSb.toString());
        outputSb = new StringBuilder(outputSb.toString().replace(" ", ""));

        while (outputSb.length() % 16 != 0) {
            outputSb.append("1");
        }
        /* Byte array conversion */
        output = new byte[outputSb.length() / 8];
        for (int i = 0; i < output.length; i++) {
            Byte b = (byte) (Integer.valueOf(outputSb.substring(i * 8, i * 8 + 8), 2) & 0xFF);
            output[i] = b;
        }
        Console.logger().finest("output bytes length = " + output.length);
        //Console.logger().finest("output = " + bytesToHex(output));
        return output;
    }

    private void saveHistoryMaps(MapBlock leftBlock, MapBlock upperBlock, int blockCursor, MapBlock block) {
        if (blockCursor > 0) {
            saveBlockToLeftStackMap(leftBlock.getIndex(), block);
        } else {
            saveBlockToLeftStackMap(0, block);
        }
        if (blockCursor >= MapLayout.BLOCK_WIDTH) {
            saveBlockToUpperStackMap(upperBlock.getIndex(), block);
        } else {
            saveBlockToUpperStackMap(0, block);
        }
    }

    private int getLeftHistoryIndex(int leftHistoryCursor, MapBlock block) {
        int index = -1;
        MapBlock[] stack = leftHistoryMap[leftHistoryCursor];
        if (stack[0] == null) {
            return index;
        } else {
            for (int i = 0; i < 4; i++) {
                if (block.equalsIgnoreTiles(stack[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private int getUpperHistoryIndex(int upperHistoryCursor, MapBlock block) {
        int index = -1;
        MapBlock[] stack = upperHistoryMap[upperHistoryCursor];
        if (stack[0] == null) {
            return index;
        } else {
            for (int i = 0; i < 4; i++) {
                if (block.equalsIgnoreTiles(stack[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private String produceFlagBits(int flags) {
        String flagBits;
        switch (flags) {
            case 0:
                flagBits = "00";
                break;
            case 0xC000:
                flagBits = "01";
                break;
            case 0x4000:
                flagBits = "100";
                break;
            case 0x8000:
                flagBits = "101";
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("11");
                for (int i = 0; i < 6; i++) {
                    if (((flags >> (15 - i)) & 1) == 0) {
                        sb.append("0");
                    } else {
                        sb.append("1");
                    }
                }
                flagBits = sb.toString();
                break;
        }
        return flagBits;
    }
}
