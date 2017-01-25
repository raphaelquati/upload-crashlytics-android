package uk.lyptt.uploadcrashlyticsandroid;

import com.crashlytics.api.AppRelease;
import com.crashlytics.api.AuthenticationException;
import com.crashlytics.api.DistributionData;
import com.crashlytics.api.ota.ReleaseNotes;
import com.crashlytics.reloc.com.google.common.base.Strings;
import com.crashlytics.tools.android.DeveloperTools;
import com.github.rvesse.airline.HelpOption;
import com.github.rvesse.airline.SingleCommand;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.parser.errors.ParseException;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
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

	@Option(name = "--app-name", description = "The application's name")
	private String appName;

	@Option(name = "--package-name", description = "The application's package name")
	private String packageName;

	@Option(name = "--version", description = "The application's version number")
	private String versionNumber;

	@Option(name = "--build-version", description = "The application's build version number")
	private String buildNumber;

	@Option(name = "--apk-path", description = "The path to the APK to upload")
	private String apkPath;

	@Arguments(description = "Additional arguments")
	private List<String> args;

	public static void main(String [] args) {
		final SingleCommand<Main> parser = SingleCommand.singleCommand(Main.class);
		try {
			final Main cmd = parser.parse(args);
			cmd.run();
		} catch (ParseException ex) {
			System.out.println(ex.getLocalizedMessage());
			System.exit(-1);
		}
	}

	private void run() {
		if (helpOption.showHelpIfRequested()) {
			return;
		}

		// Do manual validation, since the args parser isn't particularly helpful in this regard.
		if (Arrays.stream(new Object[] {
			apiKey,
			buildSecret,
			appName,
			packageName,
			versionNumber,
			buildNumber,
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

		// Create the WebApi, it defaults to null so we create it the same way the DeveloperTools private
		// processArgsInternal function does.
		DeveloperTools.setWebApi(DeveloperTools.createWebApi());

		// Now that WebApi is intialised, we can access it to upload our distribution.
		try {
			DeveloperTools.getWebApi().createDistribution(
				apiKey,
				buildSecret,
				new AppRelease(appName, packageName, UUID.randomUUID().toString(), versionNumber, buildNumber),
				new DistributionData(apkFile, new Date().toInstant().toEpochMilli() / 1000),
				false, // sendNotifications
				(length, count) -> System.out.println(String.format("%d", (count / length) * 100))
			);
		} catch (AuthenticationException | IOException e) {
			e.printStackTrace();
		}
	}
}
