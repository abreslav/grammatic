import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.abreslav.grammatic.atf.java.parser.ATFJavaModuleLoader;
import org.abreslav.grammatic.atf.java.parser.JavaTypeSystemBuilder;
import org.abreslav.grammatic.emfutils.ResourceLoader;
import org.abreslav.grammatic.utils.FileLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;


public class Filter {

	public static void main(String[] args) throws IOException {
//		filter();
		JavaTypeSystemBuilder typeSystemBuilder = new JavaTypeSystemBuilder();
		ATFJavaModuleLoader javaModuleParser = new ATFJavaModuleLoader(new FileLocator(new File(".")), typeSystemBuilder);
		javaModuleParser.loadTypeSystemModule("java.lang.typesystem");
		List<EPackage> packages = typeSystemBuilder.getEPackages();
		new ResourceLoader(".").save("java.lang.xmi", packages.toArray(new EObject[packages.size()]));
	}

	static void filter() throws FileNotFoundException, IOException {
		FileInputStream in = new FileInputStream("cat");
		StringBuilder builder = new StringBuilder();
		int c;
		while ((c = in.read()) >= 0) {
			builder.append((char) c);
		}
		in.close();
		String string = builder.toString().replaceAll("\\sinterface\\s", " class ");
//		string = string.replaceAll("^\\s*?\\*.*?", " ");
//		System.out.println(string);
		
		Pattern pattern = Pattern.compile("public(\\s|\\w)*?(class(\\s|.)*?)\\{");
		Matcher matcher = pattern.matcher(string);
		int i = 0;
		while (matcher.find()) {
			String str = matcher.group(2)
				.replaceAll("\\s+", " ")
				.trim()
				.replaceAll("(extends.*?)\\simplements", "$1,")
				.replaceAll("implements", "extends")
				.replaceAll(">>", "> >");
			System.out.println(str + ";");
			i++;
		}
		System.out.println(i);
	}
}
