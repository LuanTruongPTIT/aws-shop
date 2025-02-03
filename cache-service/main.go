// // cmd/main.go
// package main

// import (
// 	"context"

// 	"github.com/labstack/echo/v4"
// 	"github.com/labstack/echo/v4/middleware"
// 	"github.com/yourusername/cache-consistency-service/internal/config"
// 	"github.com/yourusername/cache-consistency-service/internal/handlers"
// 	"github.com/yourusername/cache-consistency-service/pkg/database"
// 	"github.com/yourusername/cache-consistency-service/pkg/redis"
// )

// func main() {
// 	// Initialize Echo
// 	e := echo.New()

// 	// Middleware
// 	e.Use(middleware.Logger())
// 	e.Use(middleware.Recover())
// 	e.Use(middleware.CORS())

// 	// Load config
// 	cfg := config.Load()

// 	// Initialize MongoDB
// 	mongoClient := database.NewMongoClient(cfg.MongoURI)
// 	defer mongoClient.Disconnect(context.Background())

// 	// Initialize Redis
// 	redisClient := redis.NewRedisClient(cfg.RedisURI)
// 	defer redisClient.Close()

// 	// Initialize handlers
// 	cacheHandler := handlers.NewCacheHandler(mongoClient, redisClient)

// 	// Routes
// 	api := e.Group("/api")
// 	{
// 		v1 := api.Group("/v1")
// 		{
// 			cache := v1.Group("/cache")
// 			{
// 				cache.POST("/invalidate", cacheHandler.HandleInvalidation)
// 				cache.GET("/check", cacheHandler.CheckConsistency)
// 				cache.GET("/status", cacheHandler.GetStatus)
// 			}
// 		}
// 	}

// 	// Start server
// 	e.Logger.Fatal(e.Start(":8080"))
// }
