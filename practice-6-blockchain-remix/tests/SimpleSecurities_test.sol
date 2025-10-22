// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "remix_tests.sol";
import "remix_accounts.sol";
// Убедитесь, что этот путь к вашему контракту правильный
import "../contracts/SimpleSecurities.sol";

contract TestSimpleSecurities {

    SimpleSecurities token; // Объявляем переменную для контракта здесь
    uint256 constant INITIAL_SUPPLY = 1000;

    // beforeEach создает НОВЫЙ экземпляр контракта ПЕРЕД каждым тестом.
    // msg.sender внутри конструктора будет правильным (тот, кто запускает тест).
    function beforeEach() public {
        token = new SimpleSecurities(INITIAL_SUPPLY);
    }

    // 1. Тест: Начальное состояние контракта
    function test_InitialState() public {
        address owner = TestsAccounts.getAccount(0);
        Assert.equal(token.owner(), owner, "Test 1 Failed: Owner is not account 0");
        Assert.equal(token.balances(owner), INITIAL_SUPPLY, "Test 1 Failed: Owner balance is not initial supply");
    }

    // 2. Тест: Успешная покупка токенов
    /// #sender: account-1
    /// #value: 1 ether
    function test_BuyTokens() public payable {
        token.buy{value: msg.value}();
        uint expectedTokens = 1 ether / token.PRICE_PER_TOKEN();
        Assert.equal(token.balances(msg.sender), expectedTokens, "Test 2 Failed: Buyer did not receive tokens");
    }

    // 3. Тест: Успешный перевод токенов от владельца
    /// #sender: account-0
    function test_TransferTokens() public {
        address recipient = TestsAccounts.getAccount(1);
        uint amount = 100;
        token.transfer(recipient, amount);
        Assert.equal(token.balances(recipient), amount, "Test 3 Failed: Recipient did not receive tokens");
    }

    // 4. Тест: Вывод средств владельцем
    /// #sender: account-0
    /// #value: 1 ether
    function test_OwnerWithdraws() public payable {
        // Шаг 1: Пополняем контракт. Владелец (account-0) сам вызывает buy(),
        // используя 1 ETH, отправленный в тест через аннотацию #value.
        token.buy{value: msg.value}();

        Assert.equal(address(token).balance, 1 ether, "Test 4 Failed: Contract balance is not 1 ether before withdrawal");

        // Шаг 2: Владелец выводит средства.
        token.withdraw();

        Assert.equal(address(token).balance, 0, "Test 4 Failed: Contract balance is not 0 after withdrawal");
    }

    // 5. Тест: Ошибка при выводе средств не-владельцем
    /// #sender: account-1
    function test_WithdrawRevertsForNonOwner() public {
        bool didRevert = false;
        try token.withdraw() {
            // Этот код не должен выполниться.
        } catch {
            didRevert = true;
        }
        Assert.ok(didRevert, "Test 5 Failed: Withdraw did not revert for non-owner");
    }

    // 6. Тест: Ошибка при переводе с нулевым балансом
    /// #sender: account-2
    function test_TransferRevertsWithNoBalance() public {
        address recipient = TestsAccounts.getAccount(3);
        bool didRevert = false;
        try token.transfer(recipient, 10) {
            // Этот код не должен выполниться.
        } catch {
            didRevert = true;
        }
        Assert.ok(didRevert, "Test 6 Failed: Transfer did not revert with zero balance");
    }
}
