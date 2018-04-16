///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package mesor.menu.principal.sistema;
//
//import java.io.File;
//import javax.swing.event.TreeModelListener;
//import javax.swing.tree.TreeModel;
//import javax.swing.tree.TreePath;
//
///**
// * ModeloArvore.java
// *
// * @version 1.0 29/03/2018
// * @author Rubens Júnior
// */
//public class ModeloArvore implements TreeModel {
//
//    protected String root;
//    /**
//     * Construtores.
//     */
//    public ModeloArvore() {}
//    public ModeloArvore(String root) {  this.root = root;   }
//    
//    // -------------------------------------------------------------------------
//    // Métodos.
//    // -------------------------------------------------------------------------
//
//    @Override
//    public Object getRoot() {
//        return root;
//    }
//
//    @Override
//    public Object getChild(Object parent, int index) {
//    }
//
//    @Override
//    public int getChildCount(Object parent) {
//        String[] children = ((String)parent).list();
//        if (children == null) return 0;
//        return children.length;
//    }
//
//    @Override
//    public boolean isLeaf(Object node) {
////        return ((File)node).isFile();
//        return true;
//    }
//    
//    @Override
//    public void valueForPathChanged(TreePath path, Object newValue) {
//    }
//
//    @Override
//    public int getIndexOfChild(Object parent, Object child) {
//    }
//
//    @Override
//    public void addTreeModelListener(TreeModelListener l) {
//    }
//
//    @Override
//    public void removeTreeModelListener(TreeModelListener l) {
//    }
//    
//}
