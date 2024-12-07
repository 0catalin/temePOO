package org.poo.parsers.commands;

import org.poo.parsers.fileio.CommandInput;

/**
 * factory for the commands, it creates the objects based on the name of the command
 */
public abstract class CommandFactory {
    /**
     * creates the objects based on the name of the command
     * @param commandInput the input of the command
     * @return a new command object depending on the input command name
     */
    public static Command createCommand(final CommandInput commandInput) {
        if (commandInput.getCommand().equals("printUsers")) {
            return new PrintUsers(commandInput);
        }
        if (commandInput.getCommand().equals("printTransactions")) {
            return new PrintTransactions(commandInput);
        }
        if (commandInput.getCommand().equals("addAccount")
                && commandInput.getAccountType().equals("classic")) {
            return new AddClassicAccount(commandInput);
        }
        if (commandInput.getCommand().equals("addAccount")
                && commandInput.getAccountType().equals("savings")) {
            return new AddSavingsAccount(commandInput);
        }
        if (commandInput.getCommand().equals("addFunds")) {
            return new AddFunds(commandInput);
        }
        if (commandInput.getCommand().equals("createCard")) {
            return new CreateCard(commandInput);
        }
        if (commandInput.getCommand().equals("createOneTimeCard")) {
            return new CreateOneTimeCard(commandInput);
        }
        if (commandInput.getCommand().equals("deleteAccount")) {
            return new DeleteAccount(commandInput);
        }
        if (commandInput.getCommand().equals("deleteCard")) {
            return new DeleteCard(commandInput);
        }
        if (commandInput.getCommand().equals("setMinimumBalance")) {
            return new SetMinimumBalance(commandInput);
        }
        if (commandInput.getCommand().equals("checkCardStatus")) {
            return new CheckCardStatus(commandInput);
        }
        if (commandInput.getCommand().equals("payOnline")) {
            return new PayOnline(commandInput);
        }
        if (commandInput.getCommand().equals("sendMoney")) {
            return new SendMoney(commandInput);
        }
        if (commandInput.getCommand().equals("setAlias")) {
            return new SetAlias(commandInput);
        }
        if (commandInput.getCommand().equals("addInterest")) {
            return new AddInterest(commandInput);
        }
        if (commandInput.getCommand().equals("changeInterestRate")) {
            return new ChangeInterestRate(commandInput);
        }
        if (commandInput.getCommand().equals("report")) {
            return new Report(commandInput);
        }
        if (commandInput.getCommand().equals("spendingsReport")) {
            return new SpendingsReport(commandInput);
        }
        if (commandInput.getCommand().equals("splitPayment")) {
            return new SplitPayment(commandInput);
        }
        return null;
    }
}
