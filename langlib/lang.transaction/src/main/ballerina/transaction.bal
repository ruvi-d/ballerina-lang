// Copyright (c) 2020 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
//
// WSO2 Inc. licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except
// in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import ballerina/java;

# Information about a transaction that does not change
# after the transaction is started.
//public type Info readonly & record {|
public type Info record {|
   # Unique identifier for the transaction branch
   byte[] xid;
   // non-zero means this transaction was a retry of
   // a previous one
   # The number of previous attempts in a sequence of retries
   int retryNumber;
   # Information about the previous attempt in a sequence of retries.
   # This will be `()` if the `retryNumber` is 0.
   Info? prevAttempt;
   # The time at which the transaction was started.
   Timestamp startTime;
|};

# An instant in time.
// todo use readonly qualifier once #27501 fixed
//public type Timestamp readonly & object {
public type Timestamp object {
    # Returns milliseconds since 1970-01-01T00:00:00Z, not including leap seconds
    public function toMillisecondsInt() returns int;
    # Returns a string representation of the timestamp in ISO 8601 format
    public function toString() returns string;
};

# Returns information about the current transaction
public transactional isolated function info() returns Info = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.Info",
    name: "info"
} external;

# Returns information about the transaction with
# the specified xid.
public isolated function getInfo(byte[] xid) returns Info? = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.GetInfo",
    name: "getInfo"
} external;

# Prevents the global transaction from committing successfully.
# This ask the transaction manager that when it makes the decision
# whether to commit or rollback, it should decide to rollback.
#
# + error - the error that caused the rollback or `()`, if there is none
public transactional isolated function setRollbackOnly(error? e) {
    if(e is error) {
      TransactionError trxError = prepareError(e.message(), e);
      wrapRollbackError(trxError);
    }
}

# Tells whether it is known that the transaction will be rolled back.
# + return - true if it is known that the transaction manager will,
# when it makes the decision whether to commit or rollback, decide
# to rollback
public transactional isolated function getRollbackOnly() returns boolean = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.GetRollbackOnly",
    name: "getRollbackOnly"
} external;

# Associates some data with the current transaction branch.
public transactional isolated function setData((any|error) & readonly e) = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.SetData",
    name: "setData"
} external;

# Retrieves data associated with the current transaction branch.
# The data is set using `setData`.
# + return - the data, or `()` if no data has been set.
public transactional isolated function getData() returns (any|error) & readonly = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.GetData",
    name: "getData"
} external;

# Type of a commit handler function.
# + info - information about the transaction being committed
public type CommitHandler function(Info info);

# Type of a rollback handler function.
# + info - information about the transaction being committed
# + cause - an error describing the cause of the rollback, if there is
# + willRetry - true if the transaction will be retried, false otherwise
public type RollbackHandler function(Info info, error? cause, boolean willRetry);

# Adds a handler to be called if and when the global transaction commits.
#
# + handler - the function to be called on commit
public transactional isolated function onCommit(CommitHandler handler) = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.OnCommit",
    name: "onCommit"
} external;

# Adds a handler to be called if and when the global transaction rolls back.
#
# + handler - the function to be called on rollback
public transactional isolated function onRollback(RollbackHandler handler) = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.OnRollback",
    name: "onRollback"
} external;

function wrapRollbackError(Error? e) = @java:Method {
    'class: "org.ballerinalang.langlib.transaction.WrapRollbackError",
    name: "wrapRollbackError"
} external;
