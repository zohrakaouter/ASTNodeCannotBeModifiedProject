package errorretrieve.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;




import org.eclipse.jface.dialogs.MessageDialog;

public class SampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"ErrorRetrieve",
				"Hello, Eclipse world");
		System.out.println("here is plugin");
		try 
        {
        	IPath path = new Path("AddressBook");
        	int start;
        	int end;
        	//IPath path = new Path("org.eclipse.ocl.xtext.base");
        	
        	  IProject myproject = ResourcesPlugin.getWorkspace().getRoot().getProject(path.lastSegment());
        	  
              IProgressMonitor myProgressMonitor=new NullProgressMonitor();
              myproject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, myProgressMonitor);
              
              if (myproject.hasNature(JavaCore.NATURE_ID)) {
     			    IJavaProject javaProject = JavaCore.create(myproject);
     			    javaProject.open(null);
     			 
     			    //javaProject.getResource().refreshLocal(IResource.DEPTH_INFINITE, null);
     			    
     			    for(IPackageFragment packageFrag : javaProject.getPackageFragments()){
     			    	
     			    	if(packageFrag.getPath().getFileExtension() == null){
     			    		
     			    		
     			    		for(IJavaElement javaEle : packageFrag.getChildren()){
     			    			
     			    			if(javaEle instanceof ICompilationUnit){//alternativley check if the element kind is == 5, it is CompilationUnit
     			    				ICompilationUnit compilUnit = (ICompilationUnit) javaEle;
     			    				if (compilUnit != null) {
     			    				 System.out.println("Compilation unit : "+compilUnit.getElementName());
     			    				
     			    	            IMarker[] ml =findJavaProblemMarkers(compilUnit);
     			    				 System.out.println("ERRORS' Number: "+ml.length); 
     			    				 for (int i = 0; i < ml.length; ++i) {
     			    					 
     			    				 System.out.println("Error found "+ml[i].toString());
     			    				 
     			    				 /** ast part **/
     			    				CompilationUnit astRoot = getAstRoot(compilUnit);
     			    				AST ast = astRoot.getAST();
     			    				 start = ml[i].getAttribute(IMarker.CHAR_START, 0);
       			    			     end = ml[i].getAttribute(IMarker.CHAR_END, 0);
       			    			     
       			    			  NodeFinder nf = new NodeFinder(astRoot, start, end-start);
    			    			     ASTNode an=nf.getCoveringNode();
    			    			    
    			    			   
    			    			     System.out.println(" ASTNode ERROR: "+an);
    			    			     
    			    			    
    			    			     /** ADD METHOD **/
    			    			  /*   ASTNode astNodeWithMethodBody = createAstNodeWithMethodBody();        

    			    			       
    			    			        MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
    			    			        System.out.println(" point 1");
    			    			        methodDeclaration.setName(ast.newSimpleName("myMethod"));
    			    			        System.out.println(" point 2");
    			    			        
    			    			        ASTNode convertedAstNodeWithMethodBody = ASTNode.copySubtree(ast, astNodeWithMethodBody);
    			    			        System.out.println(" point 3");
    			    			        Block block = (Block)convertedAstNodeWithMethodBody;
    			    			        System.out.println(" point 4");
    			    			        methodDeclaration.setBody(block);
    			    			        System.out.println(" point 5");

    			    			       
    			    			        TypeDeclaration typeDeclaration = ast.newTypeDeclaration();
    			    			        typeDeclaration.setName(ast.newSimpleName("Example"));
    			    			        typeDeclaration.bodyDeclarations().add(methodDeclaration);
    			    			        astRoot.types().add(typeDeclaration);
    			    			        */
    			    			        /** END ADD METHOD **/ 
    			    			     
    			    			     /**  add method declaration **/
    			    			     
    			    			     
    			    			     
    			    			     /**  end method declaration **/
    			    			     /** Rename type**/
    			    			     SimpleName sn= ast.newSimpleName("rename_type");
    			    			     sn.setIdentifier("newidentifier");
    			    			     
    			    			     
    			    			     System.out.println(" Identifier  "+sn.getIdentifier());
    			    			     
    			    			     if(an instanceof SimpleName)
    			    			     {
    			    			    	 System.out.println("  an is SimpleName");
    			    			    	 ((SimpleName)an).setIdentifier("person2");
    			    			     }
    			    			     else
    			    			     if( an.getParent() instanceof SimpleType)
    			    			     {
    			    			    	 System.out.println(" here is simpletype ok ");
    			    			    	 SimpleType st= (SimpleType)an.getParent(); 
    			    			      
    			    			         Name n = ast.newName("newname");
    			    			         System.out.println("Qualified name 1"+ st.getName().getFullyQualifiedName());
    			    			        
    			    			
    			    			    	 st.setName(sn);// exception 
    			    			    	 System.out.println("Qualified name 2  "+ st.getName().getFullyQualifiedName());
        			    			     
    			    			    	 
    			    			    	 
 			    			         
    			    			     }
    			    			     /** END rename type **/
        		       			     
    			    			    /** AST part end **/
    			    			   
       			    			     
     			    				 }
     			    				}
     			    				
     			    				
     			    			}
     			    		}
     			    				 
     			    	}
     			    }
              }
    	} catch(CoreException e){
   			System.out.println("\n ******** Project does not existsi or is not java ******** \n");
   			e.printStackTrace();
   		}
		return null;
	}
     			    				public IMarker[] findJavaProblemMarkers(ICompilationUnit cu) 
     			    					      throws CoreException {
     			    				
     			    					 System.out.println(" Compilation unit path : "+ cu.getPath());
     			    					 
     			    					      IResource javaSourceFile = cu.getUnderlyingResource();
     			    					      System.out.println("check ressource "+ javaSourceFile.getFileExtension());
     			    					      IMarker[] markers = 
     			    					         javaSourceFile.findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER,
     			    					            true, IResource.DEPTH_INFINITE);
     			    					     if(markers.length==0)
     			    					      System.out.println("No error detected ");
     			    					      return markers;

     			    					   }
     			    				private CompilationUnit getAstRoot(ITypeRoot typeRoot) {
     			    					  CompilationUnit root = SharedASTProvider.getAST(typeRoot, SharedASTProvider.WAIT_YES, null);
     			    					  if (root == null) {
     			    					    ASTParser astParser = ASTParser.newParser(AST.JLS_Latest);
     			    					    astParser.setSource(typeRoot);
     			    					    astParser.setResolveBindings(true);
     			    					    astParser.setStatementsRecovery(true);
     			    					   astParser.setKind(ASTParser.K_COMPILATION_UNIT);
     			    					    astParser.setBindingsRecovery(true);
     			    					   
     			    					    root = (CompilationUnit)astParser.createAST(null);
     			    					  }
     			    					  return root;  // may return null if no source available for typeRoot
     			    					}
     			    				private static ASTNode createAstNodeWithMethodBody()
     			    			    {
     			    			        String body = "int a = 1;\n int b = 2;\n return (a + b);";
     			    			        ASTParser parser = ASTParser.newParser(AST.JLS8);
     			    			        parser.setKind(ASTParser.K_STATEMENTS);
     			    			        parser.setSource(body.toCharArray());
     			    			        ASTNode result = parser.createAST(null);
     			    			        return result;
     			    			    }
}
