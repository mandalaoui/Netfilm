
class MapCommands: {
    public:
        map<string, Icommand*> commands;

        Icommand* add = new Add();
        commands["add"] = add;

        Icommand* recommend = new Recommend();
        commands["recommend"] = recommend;

        Icommand* help = new Help();
        commands["help"] = help;

        void delete() {
            delete add;
            delete recommend;
            delete help;
        }
}