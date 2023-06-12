/**
 * Adapt id for Symfony api platform, Hydra is an extension to JSON-LD
 * the id is a path
 */
fun Long.toHydraUserId(): String {
    return "api/users/$this"
}