package uk.lyptt.uploadcrashlyticsandroid;

import com.crashlytics.api.WebApi;
import com.crashlytics.tools.android.DeveloperTools;
import com.crashlytics.tools.android.DistributionTasks;
import com.crashlytics.tools.android.exception.DistributionException;
import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.parser.errors.ParseException;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.*;

@Command(name = "upload-crashlytics-android", description = "Crashlytics uploader command line tool for Android APKs")
public class Main {
	@Inject
	private HelpOption helpOption;

	@Option(name = "--api-key", description = "Your Fabric API key")
	private String apiKey;

	@Option(name = "--build-secret", description = "Your Fabric build secret")
	private String buildSecret;

	@Option(name = "--apk-path", description = "The path to the APK to upload")
	private String apkPath;

	@Option(name = "--release-notes", description = "The release notes for this version")
	private String releaseNotes;
	
	@Option(name = "--emails", description = "Emails to invite")
	private String emails = null;

	@Option(name = "--groups", description = "Groups to invite")
	private String groups = null;
		
	@Option(name = "--notify", description = "Send notification?")
	private boolean notify = false;
	
	
	@Arguments(description = "Additional arguments")
	private List<String> args;

	public static void main(String [] args) {
		final SingleCommand<Main> parser = SingleCommand.singleCommand(Main.class);
		try {
			final Main cmd = parser.parse(args);
			cmd.run();
		} catch (ParseException | DistributionException ex) {
			System.out.println(ex.getLocalizedMessage());
			System.exit(-1);
		}
	}

	private void run() throws DistributionException {
		if (helpOption.showHelpIfRequested()) {
			return;
		}
		
		if (Arrays.stream(new Object[] {
			apiKey,
			buildSecret,
			apkPath
		}).anyMatch(Objects::isNull)) {
			helpOption.showHelp();
			return;
		}

		final File apkFile = new File(Paths.get(apkPath).toUri());
		if (!apkFile.exists()) {
			System.out.println("Unable to locate APK file. Aborting.");
			System.exit(-1);
		}
		
		WebApi webApi = DeveloperTools.createWebApi();

		DistributionTasks.uploadDistribution(apiKey, buildSecret, apkFile, emails, groups, releaseNotes, notify, webApi);
	}
}
