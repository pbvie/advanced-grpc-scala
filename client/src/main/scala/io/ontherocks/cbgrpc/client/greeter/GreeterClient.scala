/*
 * Copyright 2017 Petra Bierleutgeb
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.ontherocks.cbgrpc.client
package greeter

import io.grpc.ManagedChannel
import io.ontherocks.cbgrpc.protocol.greeter.{ GreeterGrpc, ToBeGreeted }
import monix.eval.Task

class GreeterClient(channel: ManagedChannel) {

  val stub = GreeterGrpc.stub(channel)

  def greet(personName: String): Task[String] =
    Task.deferFutureAction { implicit scheduler =>
      stub.sayHello(ToBeGreeted(Some(personName))).map(_.message)
    }

}
