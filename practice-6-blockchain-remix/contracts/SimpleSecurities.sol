// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

contract SimpleSecurities {

    address public owner;
    string public name = "Simple Security Token";
    string public symbol = "SST";
    uint256 public constant PRICE_PER_TOKEN = 0.01 ether;

    mapping(address => uint256) public balances;

    event Transfer(address indexed from, address indexed to, uint256 amount);
    event Purchase(address indexed buyer, uint256 amount, uint256 value);

    constructor(uint256 initialSupply) {
        owner = msg.sender;
        balances[owner] = initialSupply;
    }

    function transfer(address _to, uint256 _amount) public {
        require(balances[msg.sender] >= _amount, "Not enough balance");
        balances[msg.sender] -= _amount;
        balances[_to] += _amount;
        emit Transfer(msg.sender, _to, _amount);
    }

    function buy() public payable {
        uint256 amountToBuy = msg.value / PRICE_PER_TOKEN;
        require(amountToBuy > 0, "You must send ETH to buy tokens");
        balances[msg.sender] += amountToBuy;
        emit Purchase(msg.sender, amountToBuy, msg.value);
    }

    function withdraw() public {
        require(msg.sender == owner, "Only owner can withdraw");
        uint256 balance = address(this).balance;
        (bool success, ) = payable(owner).call{value: balance}("");
        require(success, "Withdrawal failed");
    }
}
