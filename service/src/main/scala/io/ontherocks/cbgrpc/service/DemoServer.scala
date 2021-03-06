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

package io.ontherocks.cbgrpc.service

import io.grpc.{ Server, ServerBuilder, ServerServiceDefinition }
import org.apache.logging.log4j.scala.Logging

trait DemoServer extends Logging {

  def start(config: ServiceConfiguration, ssd: ServerServiceDefinition): Server = {
    logger.info(s"Starting demo server on port ${config.port}")
    ServerBuilder
      .forPort(config.port)
      .addService(ssd)
      .build
      .start
  }

}
