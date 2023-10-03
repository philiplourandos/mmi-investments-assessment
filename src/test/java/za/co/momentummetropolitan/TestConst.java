package za.co.momentummetropolitan;

import org.testcontainers.utility.DockerImageName;

public final class TestConst {
    public static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:16.0-alpine");

    public static final String VALID_CLIENT_ID = "2600";
}
