package com.al3x.housing2;

import com.al3x.housing2.Controllers.ResourcePackController;
import io.jooby.Jooby;
import io.jooby.ServerOptions;
import io.jooby.handler.AssetHandler;
import io.jooby.handler.AssetSource;
import io.jooby.netty.NettyServer;

import java.io.File;

public class PlaygroundWeb extends Jooby {
    public Main main;
    public ResourcePackController resourcePackController;

    public PlaygroundWeb(Main main) {
        this.main = main;

        File packObjectsFolder = new File(main.getDataFolder(), "pack_objects");
        if (!packObjectsFolder.exists()) packObjectsFolder.mkdirs();

        ServerOptions options = new ServerOptions();
        options.setPort(main.getConfig().getInt("api_port"));
        setServerOptions(options);

        // static resources
        AssetSource source = AssetSource.create(getClassLoader(), "/static/");
        assets("/*", new AssetHandler(source));

        // add controllers here
        this.resourcePackController = new ResourcePackController(main, this);

        install(new NettyServer());
    }
}
